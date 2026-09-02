package dev.zehen.myinsta2.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    /**
     * Primary compatibility target. Keep the exact version here: fingerprints
     * are validated against this build and must not silently claim compatibility
     * with adjacent Instagram releases.
     */
    val INSTAGRAM_445 = Compatibility(
        name = "Instagram 445",
        packageName = "com.instagram.android",
        apkFileType = ApkFileType.APK,
        appIconColor = 0xE1306C,
        targets = listOf(
            AppTarget(version = "445.0.0.45.83"),
        ),
    )
}
