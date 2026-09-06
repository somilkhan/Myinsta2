package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Conservative 445 story auto-advance anchor.
 *
 * The previous implementation relied on a broad Bundle + string match inside
 * ReelViewerFragment. That is not sufficient evidence for a safe control-flow
 * replacement, so the capability remains opt-in until the exact callback is
 * confirmed against the target APK.
 */
private object StoryAutoFlipFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;"),
    strings = listOf("auto_advance"),
    custom = { method, _ ->
        method.definingClass == "Linstagram/features/stories/fragment/ReelViewerFragment;"
    },
)

@Suppress("unused")
val disableStoryAutoFlipPatch = bytecodePatch(
    name = "Disable Story Auto-Flipping",
    description = "Opt-in Instagram 445 story auto-advance control; disabled until the exact callback is validated.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Keep this anchor available for target validation without rewriting an
        // uncertain callback. An incorrect early return can break story playback.
        StoryAutoFlipFingerprint.method
    }
}
