package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Exact Instagram 445 auto-advance analytics anchor.
 *
 * ReelViewerFragment.A0k(Integer) records viewer exit reasons including
 * "auto_advance". It is NOT itself proven to be the auto-advance controller,
 * so this anchor is intentionally kept validation-only and performs no rewrite.
 */
private object StoryAutoFlipFingerprint : Fingerprint(
    definingClass = "Linstagram/features/stories/fragment/ReelViewerFragment;",
    name = "A0k",
    returnType = "V",
    parameters = listOf("Ljava/lang/Integer;"),
    strings = listOf("exit_viewer", "auto_advance", "swipe"),
)

@Suppress("unused")
val disableStoryAutoFlipPatch = bytecodePatch(
    name = "Disable Story Auto-Flipping",
    description = "Opt-in Instagram 445 story auto-advance research anchor; disabled until the exact controller callback is validated.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // A0k is an exact 445 analytics/exit-reason method, not yet a proven
        // playback controller. Do not alter its control flow.
        StoryAutoFlipFingerprint.method
    }
}
