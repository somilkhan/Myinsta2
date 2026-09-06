package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Exact Instagram 445 candidate for the story auto-flipping controller.
 *
 * ReelViewerFragment.Fji(Object) matches the known story auto-flipping
 * fingerprint shape (return V, Object parameter, and "userSession" string).
 * The controller role is not yet runtime-validated, so this remains a
 * validation-only anchor and performs no rewrite.
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
    description = "Opt-in Instagram 445 story auto-advance research anchor; disabled until the exact controller callback is runtime-validated.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Fji is an exact 445 structural candidate, but its runtime control
        // flow is not yet proven safe to rewrite. Keep this anchor validation-only.
        StoryAutoFlipFingerprint.method
    }
}
