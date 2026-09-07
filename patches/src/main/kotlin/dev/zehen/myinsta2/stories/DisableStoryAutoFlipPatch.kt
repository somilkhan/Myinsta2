package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Instagram 445 story-timeout callback.
 *
 * The patch uses raw smali insertion instead of Morphe's returnEarly()
 * helper because stripped Morphe runtimes do not contain BytecodeUtilsKt.
 */
private object StoryAutoFlipFingerprint : Fingerprint(
    definingClass = "Linstagram/features/stories/fragment/ReelViewerFragment;",
    name = "Fji",
    returnType = "V",
    parameters = listOf("Ljava/lang/Object;"),
    strings = listOf("userSession"),
)

@Suppress("unused")
val disableStoryAutoFlipPatch = bytecodePatch(
    name = "Disable Story Auto-Flipping",
    description = "Disable stories automatically flipping/skipping after the timeout.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        StoryAutoFlipFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
