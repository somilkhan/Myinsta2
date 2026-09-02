package dev.zehen.myinsta2

import app.morphe.patcher.patch.Compatibility

/** Compatibility gate for the Instagram build this patch set is maintained against. */
val instagram445 = Compatibility(
    name = "Instagram",
    packageName = "com.instagram.android",
    targets = listOf("445.0.0.45.83"),
)
