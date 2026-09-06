package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.ads.hideAdsPatch
import dev.zehen.myinsta2.download.downloadMediaPatch
import dev.zehen.myinsta2.ghostmode.antiRevokePatch
import dev.zehen.myinsta2.ghostmode.ghostModePatch
import dev.zehen.myinsta2.ghostmode.ghostModeTypingPatch
import dev.zehen.myinsta2.ghostmode.viewStoriesAnonymouslyPatch
import dev.zehen.myinsta2.stories.disableStoryAutoFlipPatch
import dev.zehen.myinsta2.stories.disableVideoAutoplayPatch

/** MyInsta2 feature bundle for Instagram 445. */
@Suppress("unused")
val myInsta2Patch = bytecodePatch(
    name = "MyInsta2",
    description = "MyInsta feature bundle integrated into Instagram 445.",
    default = true,
) {
    dependsOn(
        downloadMediaPatch,
        ghostModePatch,
        ghostModeTypingPatch,
        viewStoriesAnonymouslyPatch,
        antiRevokePatch,
        hideAdsPatch,
        disableStoryAutoFlipPatch,
        disableVideoAutoplayPatch,
    )
}
