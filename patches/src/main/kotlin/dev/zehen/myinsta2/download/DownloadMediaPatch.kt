package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Safe 445 downloader integration boundary.
 *
 * Instagram's 445 overflow implementation is heavily obfuscated and the
 * available target evidence identifies the menu/helper classes, but does not
 * yet prove a complete, type-safe dispatch path for injecting a new option.
 * Keep the exact anchors available without emitting unverifiable calls into
 * the target APK.
 */
private object MediaOptionsOverflowHelperFingerprint : Fingerprint(
    name = "A09",
    definingClass = "LX/Zxv;",
    parameters = listOf("Lcom/instagram/feed/media/mediaoption/MediaOption\$Option;"),
    returnType = "V",
    strings = listOf("MediaOptionsOverflowHelper"),
)

private object ReelMoreOptionsFingerprint : Fingerprint(
    definingClass = "LX/9Tx;",
    returnType = "V",
    strings = listOf("ClipsOrganicMediaItemViewMoreOptionsController"),
)

@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "445 media-download integration anchors; dispatch remains disabled until the exact menu callback is validated.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Resolve the anchors during patching so target drift fails loudly,
        // rather than silently producing a broken Instagram APK.
        MediaOptionsOverflowHelperFingerprint.method
        ReelMoreOptionsFingerprint.method
    }
}
