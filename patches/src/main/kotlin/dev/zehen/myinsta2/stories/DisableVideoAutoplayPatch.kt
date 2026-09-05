package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Instagram's video-autoplay experiment predicate on 445. */
private object DisableVideoAutoplayFingerprint : Fingerprint(
    returnType = "Z",
    strings = listOf(
        "ig_olympus_disable_video_autoplay",
        "ig_disable_video_autoplay",
        "ig_video_setting",
    ),
)

@Suppress("unused")
val disableVideoAutoplayPatch = bytecodePatch(
    name = "Disable video autoplay",
    description = "Disables Instagram video autoplay through the 445 video-setting predicate.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        DisableVideoAutoplayFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn v0",
        )
    }
}
