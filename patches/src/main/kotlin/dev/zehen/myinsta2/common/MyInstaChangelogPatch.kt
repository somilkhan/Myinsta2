package dev.zehen.myinsta2.common

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445
import org.w3c.dom.Element

private const val ANDROID_NS = "http://schemas.android.com/apk/res/android"
private const val PROVIDER = "dev.zehen.myinsta2.extension.MyInstaChangelogProvider"
private const val AUTHORITY = "dev.zehen.myinsta2.changelog"

/** Adds the runtime provider used by the first-launch/update changelog UI. */
private val myInstaChangelogManifestPatch = resourcePatch {
    execute {
        document("AndroidManifest.xml").use { document ->
            val application = document.getElementsByTagName("application").item(0) as? Element
                ?: error("Instagram application node was not found")

            val providers = application.getElementsByTagName("provider")
            for (index in 0 until providers.length) {
                val existing = providers.item(index) as? Element ?: continue
                if (existing.getAttributeNS(ANDROID_NS, "authorities") == AUTHORITY) return@use
            }

            val provider = document.createElement("provider")
            provider.setAttributeNS(ANDROID_NS, "android:name", PROVIDER)
            provider.setAttributeNS(ANDROID_NS, "android:authorities", AUTHORITY)
            provider.setAttributeNS(ANDROID_NS, "android:exported", "false")
            application.appendChild(provider)
        }
    }
}

@Suppress("unused")
val myInstaChangelogPatch = bytecodePatch(
    name = "MyInsta2 — Changelog",
    description = "Shows the MyInsta2 changelog when the app is first opened and after an update.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)
    dependsOn(myInstaChangelogManifestPatch)
    extendWith("extensions/myinsta.mpe")
}
