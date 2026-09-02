package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Suppresses the 445 notification receiver path dedicated to revoked-message
 * notifications. This mirrors MyInsta's anti-revoke-notification behavior:
 * Instagram still owns the message state, but the revoke notification handler
 * is not allowed to surface the notification to the user.
 */
private object RevokedMessageNotificationFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf(
        "Landroid/content/Context;",
        "Landroid/content/Intent;",
        "LX/2ej;",
        "Lcom/instagram/common/session/UserSession;",
        "LX/72e;",
        "Lkotlin/jvm/functions/Function0;",
    ),
    strings = listOf("message_revoked", "revoke_notification"),
)

@Suppress("unused")
val antiRevokePatch = bytecodePatch(
    name = "Anti Revoke — notification",
    description = "Suppresses Instagram's revoked-message notification handler.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        RevokedMessageNotificationFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
