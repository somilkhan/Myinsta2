package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val myInsta2Patch =
    bytecodePatch(
        name = "MyInsta2",
        description = "MyInsta features and UI integrated into Instagram.",
    ) {
        compatibleWith(instagram445)
    }
