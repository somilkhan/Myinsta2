package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.ads.hideAdsPatch
import dev.zehen.myinsta2.ghostmode.antiRevokePatch
import dev.zehen.myinsta2.ghostmode.ghostModePatch
import dev.zehen.myinsta2.ghostmode.ghostModeTypingPatch

/** MyInsta2 production feature bundle for Instagram 445. */
@Suppress("unused")
val myInsta2Patch = bytecodePatch(
    name = "MyInsta2",
    description = "Validated MyInsta2 features for Instagram 445.",
    default = true,
) {
    // Only fingerprints verified against the supplied 445 APK are promoted
    // into the aggregate. Other implemented patches remain independently
    // selectable until their target/runtime validation is complete.
    dependsOn(
        ghostModePatch,
        ghostModeTypingPatch,
        antiRevokePatch,
        hideAdsPatch,
    )
}
