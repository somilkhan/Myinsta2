package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch

/**
 * MyInsta2 patch bundle entry point.
 *
 * Individual feature patches are top-level Morphe patch declarations. They are
 * intentionally kept independent so Morphe can report compatibility and let
 * users select only the features that match their Instagram build.
 */
@Suppress("unused")
val myInsta2Patch = bytecodePatch(
    name = "MyInsta2",
    description = "MyInsta features and UI integrated into Instagram.",
)
