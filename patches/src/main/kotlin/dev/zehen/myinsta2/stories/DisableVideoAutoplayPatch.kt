package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact Instagram 445 video-autoplay preference predicate. */
private object DisableVideoAutoplayFingerprint : Fingerprint(
    definingClass = "LX/13A;",
    name = "A00",
    returnType = "Z",
    parameters = listOf("Lcom/instagram/common/session/UserSession;"),
    strings = listOf(
        "ig_olympus_disable_video_autoplay",
        "ig_disable_video_autoplay",
        "ig_video_setting",
    ),
)

@Suppress("unused")
val disableVideoAutoplayPatch = bytecodePatch(
    name = "Disable video autoplay",
    description = "Forces the Instagram 445 video-autoplay preference predicate off.",
    // This patch is a dependency of the default MyInsta2 aggregate; Morphe
    // executes dependencies when the parent patch executes.
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        DisableVideoAutoplayFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn v0",
        )
    }
}
