package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact Instagram 445 direct-message seen request method. */
private object MarkThreadSeenFingerprint : Fingerprint(
    definingClass = "LX/JmB;",
    name = "A09",
    returnType = "V",
    parameters = listOf(
        "Lcom/instagram/common/session/UserSession;",
        "LX/1ew;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
    ),
    strings = listOf("mark_thread_seen-"),
)

/**
 * Ghost Mode DM-seen suppression. The fingerprint is deliberately exact: a
 * broad string-only match can target a logging/telemetry helper instead of the
 * request operation.
 */
@Suppress("unused")
val ghostModePatch = bytecodePatch(
    name = "Ghost Mode — DM seen",
    description = "Blocks Instagram's direct-message seen event on 445.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        MarkThreadSeenFingerprint.method.addInstructions(0, "return-void")
    }
}
