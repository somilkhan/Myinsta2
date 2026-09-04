package dev.zehen.myinsta2.revoke

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact 445 revoke-notification dispatch method. */
private object RevokeNotificationFingerprint : Fingerprint(
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
    name = "Anti Revoke",
    description = "Suppresses Instagram's revoke-notification dispatch on Instagram 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        RevokeNotificationFingerprint.method.addInstructions(0, "return-void")
    }
}
