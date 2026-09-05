package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Safe 445 download integration point.
 *
 * The Instagram 445 menu/controller anchors are intentionally kept as
 * fingerprints. We do not inject a guessed downloader implementation: the
 * menu item needs a target-side URI/media extraction path before it can safely
 * launch a save operation.
 */
private object ReelMoreOptionsGuardFingerprint : Fingerprint(
    name = "A08",
    definingClass = "LX/9Tx;",
    parameters = listOf(
        "Landroid/view/View;", "LX/1Pg;", "LX/9PM;",
        "Lcom/instagram/feed/media/Media;", "LX/9Tx;", "Z", "Z", "Z",
    ),
    returnType = "V",
    strings = listOf("ClipsOrganicMediaItemViewMoreOptionsController"),
)

@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "Download integration for Instagram 445 media menus.",
) {
    compatibleWith(INSTAGRAM_445)

    // This patch deliberately remains a no-op until the exact media URL/save
    // callback is mapped. A fingerprint-only patch is preferable to silently
    // corrupting the reel menu or wiring an incompatible 364 implementation.
    execute {
        ReelMoreOptionsGuardFingerprint.method
    }
}
