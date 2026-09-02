package dev.zehen.myinsta2.download

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Download integration boundary for Instagram 445.
 *
 * The target APK exposes downloader/menu classes documented in
 * DownloadFingerprints.kt. The exact method bodies still need instruction-
 * level mapping before a safe hook can be applied.
 */
@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "Download posts, reels, stories and DM media.",
) {
    compatibleWith(INSTAGRAM_445)
}
