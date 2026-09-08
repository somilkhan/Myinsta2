package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact Instagram 445 story-seen request builder. */
private object StorySeenRequestFingerprint : Fingerprint(
    definingClass = "LX/0hI;",
    name = "A04",
    returnType = "LX/7po;",
    parameters = listOf("Lcom/instagram/common/session/UserSession;"),
    strings = listOf("media/seen/?reel=%s&live_vod=0"),
)

@Suppress("unused")
val viewStoriesAnonymouslyPatch = bytecodePatch(
    name = "Ghost Mode — view stories anonymously",
    description = "Prevents Instagram 445 from constructing the story-seen request.",
    // This patch is a dependency of the default MyInsta2 aggregate. Morphe
    // executes dependencies unconditionally when their parent is executed, so
    // false here would incorrectly imply that this feature is opt-in.
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // A04 returns LX/7po;. All current 445 call sites ignore that result,
        // so a typed null return suppresses request construction without
        // emitting an invalid return-void.
        StorySeenRequestFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn-object v0",
        )
    }
}
