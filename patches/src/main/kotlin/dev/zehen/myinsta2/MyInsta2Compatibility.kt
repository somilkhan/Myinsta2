package dev.zehen.myinsta2

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.Compatibility

/**
 * Compatibility metadata for the supplied standalone Instagram base.apk.
 * Morphe patches the base APK directly, so this target must not require an
 * APKM/split container merely because newer Instagram distributions can be
 * delivered as bundles.
 */
internal val instagram445 = Compatibility(
    name = "Instagram",
    packageName = "com.instagram.android",
    apkFileType = ApkFileType.APK,
    targets = listOf(
        AppTarget(
            version = "445.0.0.45.83",
            description = "Stable Instagram 445 target (base.apk).",
        ),
    ),
)
