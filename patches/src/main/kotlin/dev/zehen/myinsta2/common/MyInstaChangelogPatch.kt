package dev.zehen.myinsta2.common

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445
import org.w3c.dom.Element

private const val ANDROID_NS = "http://schemas.android.com/apk/res/android"
private const val PROVIDER = "dev.zehen.myinsta2.extension.MyInstaChangelogProvider"
private const val PROVIDER_AUTHORITY = "dev.zehen.myinsta2.changelog"
private const val SETTINGS_ACTIVITY = "dev.zehen.myinsta2.extension.MyInstaSettingsActivity"

/** Exact Instagram 445 profile action-bar path. */
private object ProfileActionBarSelfSwitcherFingerprint : Fingerprint(
    definingClass = "LX/Dyw;",
    name = "A05",
    returnType = "V",
    parameters = listOf("LX/AOq;", "LX/KCa;"),
)

/** Adds runtime components used by the first-launch/update changelog and settings UI. */
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
    description = "Shows the first-launch/update changelog and exposes the verified Instagram 445 profile long-press settings surface.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)
    dependsOn(myInstaChangelogManifestPatch)
    extendWith("extensions/myinsta.mpe")

    execute {
        ProfileActionBarSelfSwitcherFingerprint.method.apply {
            val anchors = instructions.filter { instruction ->
                if (instruction.opcode != Opcode.INVOKE_STATIC) return@filter false
                val registers = instruction as? FiveRegisterInstruction ?: return@filter false
                val reference = (instruction as? ReferenceInstruction)?.reference as? MethodReference
                    ?: return@filter false
                reference.definingClass == "LX/gAr;" &&
                    reference.name == "A04" &&
                    reference.returnType == "V" &&
                    reference.parameterTypes == listOf(
                        "Landroidx/fragment/app/FragmentActivity;",
                        "LX/AOq;",
                        "Lcom/instagram/common/session/UserSession;",
                        "Ljava/lang/Integer;",
                    ) &&
                    registers.registerCount == 4 &&
                    registers.registerC == 1 &&
                    registers.registerD == 5 &&
                    registers.registerE == 12 &&
                    registers.registerF == 12
            }

            require(anchors.size == 1) {
                "MyInsta2: expected exactly one exact 445 profile action-bar anchor, found ${anchors.size}"
            }

            val anchor = anchors.single()
            addInstructions(
                anchor.location.index + 1,
                "invoke-static {v1}, Ldev/zehen/myinsta2/extension/MyInstaSettingsEntryPoint;->onProfileActionBarReady(Landroid/app/Activity;)V",
            )
        }
    }
}
