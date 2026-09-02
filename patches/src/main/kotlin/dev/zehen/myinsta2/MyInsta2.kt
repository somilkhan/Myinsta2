package dev.zehen.myinsta2

import app.morphe.patcher.patch.bytecodePatch

/** Root patch for the MyInsta2 Instagram integration. */
@Suppress("unused")
val myInsta2Patch =
    bytecodePatch(
        name = "MyInsta2",
        description = "MyInsta features and UI integrated into Instagram.",
    ) {
        compatibleWith(instagram445)
    }
