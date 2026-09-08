package dev.zehen.myinsta2.common

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445
import org.w3c.dom.Element

private const val ANDROID_NS = "http://schemas.android.com/apk/res/android"
private const val PROVIDER = "dev.zehen.myinsta2.extension.MyInstaChangelogProvider"
private const val PROVIDER_AUTHORITY = "dev.zehen.myinsta2.changelog"
private const val SETTINGS_ACTIVITY = "dev.zehen.myinsta2.extension.MyInstaSettingsActivity"

/** Adds the runtime components used by the first-launch/update changelog and settings UI. */
private val myInstaChangelogManifestPatch = resourcePatch {
    execute {
        document("AndroidManifest.xml").use { document ->
            val application = document.getElementsByTagName("application").item(0) as? Element
                ?: error("Instagram application node was not found")

            val providers = application.getElementsByTagName("provider")
            var providerPresent = false
            for (index in 0 until providers.length) {
                val existing = providers.item(index) as? Element ?: continue
                if (existing.getAttributeNS(ANDROID_NS, "authorities") == PROVIDER_AUTHORITY) {
                    providerPresent = true
                    break
                }
            }
            if (!providerPresent) {
                val provider = document.createElement("provider")
                provider.setAttributeNS(ANDROID_NS, "android:name", PROVIDER)
                provider.setAttributeNS(ANDROID_NS, "android:authorities", PROVIDER_AUTHORITY)
                provider.setAttributeNS(ANDROID_NS, "android:exported", "false")
                application.appendChild(provider)
            }

            val activities = application.getElementsByTagName("activity")
            var activityPresent = false
            for (index in 0 until activities.length) {
                val existing = activities.item(index) as? Element ?: continue
                if (existing.getAttributeNS(ANDROID_NS, "android:name") == SETTINGS_ACTIVITY) {
                    activityPresent = true
                    break
                }
            }
            if (!activityPresent) {
                val activity = document.createElement("activity")
                activity.setAttributeNS(ANDROID_NS, "android:name", SETTINGS_ACTIVITY)
                activity.setAttributeNS(ANDROID_NS, "android:exported", "false")
                application.appendChild(activity)
            }
        }
    }
}

@Suppress("unused")
val myInstaChangelogPatch = bytecodePatch(
    name = "MyInsta2 — Changelog + Settings",
    description = "Shows the first-launch/update changelog and exposes the MyInsta profile long-press settings surface.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)
    dependsOn(myInstaChangelogManifestPatch)
    extendWith("extensions/myinsta.mpe")
}
