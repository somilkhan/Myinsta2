package dev.zehen.myinsta2

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.Compatibility

internal val instagram445 = Compatibility(
    name = "Instagram",
    packageName = "com.instagram.android",
    apkFileType = ApkFileType.APKM_REQUIRED,
    targets = listOf(
        AppTarget(
            version = "445.0.0.45.83",
            description = "Stable Instagram 445 target.",
        ),
    ),
)
