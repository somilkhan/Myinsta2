package dev.zehen.myinsta2.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val INSTAGRAM_445 = Compatibility(
        name = "Instagram",
        packageName = "com.instagram.android",
        apkFileType = ApkFileType.APK,
        appIconColor = 0xE1306C,
        targets = listOf(
            AppTarget(
                version = "445.0.0.45.83",
            ),
        ),
    )
}
