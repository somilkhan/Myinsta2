package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch

/**
 * MyInsta2 integration entrypoint.
 *
 * Feature patches should be registered here only after they have a target
 * fingerprint and have been exercised against the supported Instagram build.
 */
val myInsta2Patch = bytecodePatch(
    name = "MyInsta2",
    description = "MyInsta2 privacy, media and quality-of-life patches",
) {
    // Feature patches are added as their 445-compatible implementations land.
}
