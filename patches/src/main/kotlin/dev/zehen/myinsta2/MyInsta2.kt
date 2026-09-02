package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch

/**
 * MyInsta2 patch bundle entry point.
 *
 * Target-specific feature patches live in this package and must declare their
 * own fingerprints/compatibility. The bundle itself deliberately has no
 * synthetic compatibility symbol: Morphe discovers compatibility from the
 * contained patches when applying the bundle.
 */
@Suppress("unused")
val myInsta2Patch =
    bytecodePatch(
        name = "MyInsta2",
        description = "MyInsta features and UI integrated into Instagram.",
    )
