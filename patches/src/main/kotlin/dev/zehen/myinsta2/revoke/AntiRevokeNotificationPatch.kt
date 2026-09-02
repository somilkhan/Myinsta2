package dev.zehen.myinsta2.revoke

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private object RevokeNotificationFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("revoke_notification"),
)

@Suppress("unused")
val antiRevokeNotificationPatch = bytecodePatch(
    name = "Anti-Revoke notification",
    description = "Suppresses the revoked-message notification hook on Instagram 445.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        RevokeNotificationFingerprint.method.addInstructions(0, "return-void")
    }
}
