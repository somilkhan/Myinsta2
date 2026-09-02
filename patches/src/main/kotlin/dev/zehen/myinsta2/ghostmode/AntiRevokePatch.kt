package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact Instagram 445 revoke-notification action handler. */
private object RevokedMessageNotificationFingerprint : Fingerprint(
    name = "A01",
    definingClass = "LX/72e;",
    returnType = "V",
    parameters = listOf(
        "Landroid/content/Intent;",
        "LX/2ej;",
    ),
    strings = listOf("revoke_notification"),
)

@Suppress("unused")
val antiRevokePatch = bytecodePatch(
    name = "Anti Revoke — notification",
    description = "Suppresses Instagram's revoked-message notification action on 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        RevokedMessageNotificationFingerprint.method.addInstructions(0, "return-void")
    }
}
