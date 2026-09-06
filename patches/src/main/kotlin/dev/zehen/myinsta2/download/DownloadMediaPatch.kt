package dev.zehen.myinsta2.download

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Target anchors for the 445 media-download subsystem.
 *
 * No guessed control-flow injection is performed here. The actual downloader
 * requires a complete extraction/storage implementation and a safe menu
 * dispatch hook; until those are wired, this patch is deliberately disabled
 * from the MyInsta2 production bundle.
 */
@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "Instagram 445 media-download integration boundary.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)
}
