package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Instagram 445 story-timeout callback.
 *
 * This is the same structural callback used by the established Morphe
 * implementation: Object parameter + void return + userSession string in
 * ReelViewerFragment. Returning before the timeout action prevents the
 * automatic transition while leaving manual story navigation intact.
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
        StoryAutoFlipFingerprint.method.returnEarly()
    }
}
