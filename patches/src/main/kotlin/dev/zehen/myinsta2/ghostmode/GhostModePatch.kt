package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private object MarkThreadSeenFingerprint : Fingerprint(
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

@Suppress("unused")
val ghostModePatch = bytecodePatch(
    name = "Ghost Mode — DM seen",
    description = "Blocks Instagram's direct-message seen event. Select this patch in Morphe to enable it.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Morphe patch selection is the feature toggle. Do not depend on an
        // Instagram SharedPreferences key that the base APK does not define.
        MarkThreadSeenFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
