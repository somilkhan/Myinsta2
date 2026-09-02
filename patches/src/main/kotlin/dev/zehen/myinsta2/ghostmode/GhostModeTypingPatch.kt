package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Instagram 445 routes the direct typing-indicator request through this
 * method. Returning before the request is emitted prevents the local typing
 * event from being sent while leaving ordinary messaging intact.
 *
 * This is intentionally separate from DM-seen Ghost Mode so users can enable
 * the two privacy controls independently in Morphe.
 */
private object TypingIndicatorRequestFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf(
        "LX/2mc;",
        "LX/Ovs;",
        "LX/ADU;",
    ),
    strings = listOf("direct_v2/threads/%s/toggle_typing_indicator/"),
)

@Suppress("unused")
val ghostModeTypingPatch = bytecodePatch(
    name = "Ghost Mode — typing status",
    description = "Prevents Instagram from sending the direct-message typing indicator.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        TypingIndicatorRequestFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
