package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Exact Instagram 445 story-seen URI builder hook.
 *
 * The target 445 DEX maps the request builder to LX/0hI;->A06. Keep the
 * fingerprint bound to that method rather than selecting an arbitrary boolean
 * method from the matched class.
 */
private object StorySeenUriBuilderFingerprint : Fingerprint(
    definingClass = "LX/0hI;",
    name = "A06",
    returnType = "Z",
    strings = listOf("media/seen/?reel=%s&live_vod=0"),
)

@Suppress("unused")
val viewStoriesAnonymouslyPatch = bytecodePatch(
    name = "Ghost Mode — view stories anonymously",
    description = "Prevents the story-seen request from being emitted on Instagram 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Exit immediately with false. Mutating v0 immediately before the
        // original return was unsafe because the original method could write
        // to v0 again between the injected instruction and RETURN.
        StorySeenUriBuilderFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x0
            return v0
            """.trimIndent(),
        )
    }
}
