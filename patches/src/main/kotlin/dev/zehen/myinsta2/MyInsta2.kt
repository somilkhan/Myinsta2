package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.ads.hideAdsPatch
import dev.zehen.myinsta2.distractionfree.hideSuggestedContentPatch
import dev.zehen.myinsta2.ghostmode.antiRevokePatch
import dev.zehen.myinsta2.ghostmode.ghostModePatch
import dev.zehen.myinsta2.ghostmode.ghostModeTypingPatch
import dev.zehen.myinsta2.stories.disableStoryAutoFlipPatch

/**
 * MyInsta2 patch bundle entry point.
 *
 * The bundle depends only on executable 445 implementations. Version-specific
 * mappings that are not yet proven are intentionally kept out of the bundle.
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
        hideSuggestedContentPatch,
    )
}
