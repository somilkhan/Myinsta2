package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.ads.hideAdsPatch
import dev.zehen.myinsta2.ghostmode.antiRevokePatch
import dev.zehen.myinsta2.ghostmode.ghostModePatch
import dev.zehen.myinsta2.ghostmode.ghostModeTypingPatch
import dev.zehen.myinsta2.ghostmode.viewStoriesAnonymouslyPatch

/** MyInsta2 production feature bundle for Instagram 445. */
@Suppress("unused")
val myInsta2Patch = bytecodePatch(
    name = "MyInsta2",
    description = "Validated MyInsta2 features for Instagram 445.",
    default = true,
) {
    // Only 445 targets whose class/method signatures and surrounding call
    // behavior have been verified against the supplied APK are aggregated.
    dependsOn(
        ghostModePatch,
        ghostModeTypingPatch,
        viewStoriesAnonymouslyPatch,
        antiRevokePatch,
        hideAdsPatch,
    )
}
