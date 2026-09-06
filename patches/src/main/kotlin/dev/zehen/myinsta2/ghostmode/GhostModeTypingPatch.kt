package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact Instagram 445 typing-request operation builder. */
private object TypingIndicatorRequestFingerprint : Fingerprint(
    definingClass = "LX/4tv;",
    name = "A02",
    returnType = "Ljava/util/List;",
    strings = listOf("direct_v2/threads/%s/toggle_typing_indicator_control/"),
)

@Suppress("unused")
val ghostModeTypingPatch = bytecodePatch(
    name = "Ghost Mode — typing status",
    description = "Removes Instagram's typing-indicator request operation from the 445 request list.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // A02 builds a List of network operations. Returning an immutable empty
        // list is type-correct and prevents the typing operation from being
        // scheduled without corrupting the caller's control flow.
        TypingIndicatorRequestFingerprint.method.addInstructions(
            0,
            """
            sget-object v0, Ljava/util/Collections;->EMPTY_LIST:Ljava/util/List;
            return-object v0
            """.trimIndent(),
        )
    }
}
