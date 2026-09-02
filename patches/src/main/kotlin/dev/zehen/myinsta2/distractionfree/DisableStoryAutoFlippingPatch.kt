package dev.zehen.myinsta2.distractionfree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Stops the story viewer's timeout action from automatically advancing to the
 * next story. The target method is anchored by ReelViewerFragment + userSession
 * rather than its obfuscated method name.
 */
private object StoryTimeoutActionFingerprint : Fingerprint(
    parameters = listOf("Ljava/lang/Object;"),
    returnType = "V",
    strings = listOf("userSession"),
    definingClass = "/ReelViewerFragment;",
)

@Suppress("unused")
val disableStoryAutoFlippingPatch = bytecodePatch(
    name = "Disable story auto-flipping",
    description = "Prevents Instagram Stories from automatically advancing to the next story.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        StoryTimeoutActionFingerprint.method.returnEarly()
    }
}
