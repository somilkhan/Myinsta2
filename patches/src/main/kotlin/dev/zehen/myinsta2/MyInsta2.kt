package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.ads.hideAdsPatch
import dev.zehen.myinsta2.ghostmode.antiRevokePatch
import dev.zehen.myinsta2.ghostmode.ghostModePatch
import dev.zehen.myinsta2.ghostmode.ghostModeTypingPatch
import dev.zehen.myinsta2.ghostmode.viewStoriesAnonymouslyPatch
import dev.zehen.myinsta2.stories.disableStoryAutoFlipPatch
import dev.zehen.myinsta2.stories.disableVideoAutoplayPatch

/** MyInsta2 production feature bundle for Instagram 445. */
@Suppress("unused")
val myInsta2Patch = bytecodePatch(
    name = "MyInsta2",
    description = "Validated MyInsta features for Instagram 445.",
    default = true,
) {
    dependsOn(
        ghostModePatch,
        ghostModeTypingPatch,
        viewStoriesAnonymouslyPatch,
        antiRevokePatch,
        hideAdsPatch,
        disableVideoAutoplayPatch,
        disableStoryAutoFlipPatch,
    )
}
