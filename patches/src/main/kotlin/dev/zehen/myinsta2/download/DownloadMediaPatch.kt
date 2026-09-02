package dev.zehen.myinsta2.download

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Download integration shell for Instagram 445.
 *
 * The target-specific anchors live in DownloadFingerprints.kt. This patch is
 * intentionally compatibility-gated but does not alter Instagram until the
 * exact media-object and overflow-menu method bodies are mapped. Shipping a
 * weak string-only hook here would be unsafe.
 */
@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "Download posts, reels, stories and DM media.",
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Target-specific implementation is enabled only after method-level
        // mapping is available. See DownloadFingerprints.kt and port matrix.
    }
}
