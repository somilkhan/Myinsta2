package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Exact Instagram 445 story-seen request method.
 *
 * The target method LX/0hI;->A06 returns void and contains the
 * media/seen/?reel=%s&live_vod=0 request template. Returning immediately
 * prevents the seen event from being submitted while leaving story playback
 * itself untouched.
 */
private object StorySeenRequestFingerprint : Fingerprint(
    definingClass = "LX/0hI;",
    name = "A06",
    returnType = "V",
    parameters = listOf(
        "Lcom/instagram/common/session/UserSession;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
    ),
    strings = listOf("media/seen/?reel=%s&live_vod=0"),
)

@Suppress("unused")
val viewStoriesAnonymouslyPatch = bytecodePatch(
    name = "Ghost Mode — view stories anonymously",
    description = "Prevents Instagram from submitting the story-seen request on 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        StorySeenRequestFingerprint.method.addInstructions(0, "return-void")
    }
}
