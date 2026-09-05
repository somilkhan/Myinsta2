package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.ads.hideAdsPatch
import dev.zehen.myinsta2.ghostmode.antiRevokePatch
import dev.zehen.myinsta2.ghostmode.ghostModePatch
import dev.zehen.myinsta2.ghostmode.ghostModeTypingPatch
import dev.zehen.myinsta2.stories.disableStoryAutoFlipPatch

/**
 * MyInsta2 patch bundle entry point.
 *
 * The bundle patch deliberately depends only on patches that currently have
 * executable 445 implementations. This makes selecting MyInsta2 useful while
 * keeping unfinished feature mappings out of the patch execution graph.
 */
@Suppress("unused")
val myInsta2Patch = bytecodePatch(
    name = "MyInsta2",
    description = "MyInsta features and UI integrated into Instagram 445.",
    default = true,
) {
    dependsOn(
        ghostModePatch,
        ghostModeTypingPatch,
        antiRevokePatch,
        hideAdsPatch,
        disableStoryAutoFlipPatch,
    )
}
