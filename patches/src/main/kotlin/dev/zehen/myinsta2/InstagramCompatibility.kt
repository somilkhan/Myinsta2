package dev.zehen.myinsta2

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Compatibility gate for the Instagram build this patch set is maintained against. */
val instagram445 = Compatibility(
    packageName = "com.instagram.android",
    name = "Instagram",
    apkFileType = ApkFileType.APKM,
    targets = listOf(
        AppTarget(
            version = "445.0.0.45.83",
            description = "Stable Instagram target for MyInsta2.",
        ),
    ),
)
