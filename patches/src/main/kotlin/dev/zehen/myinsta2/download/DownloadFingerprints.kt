package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint

/**
 * Instagram 445 download-related anchors confirmed in the supplied APK.
 *
 * These are deliberately fingerprints only. They are not patched until the
 * surrounding method bodies and required media-data dependencies have been
 * mapped. This prevents a string-only match from becoming a crash-prone hook.
 */
internal object MediaOptionsOverflowHelperFingerprint : Fingerprint(
    parameters = listOf("Lcom/instagram/feed/media/mediaoption/MediaOption\$Option;"),
    strings = listOf("MediaOptionsOverflowHelper"),
    returnType = "V",
)

internal object ReelMoreOptionsFingerprint : Fingerprint(
    strings = listOf("ClipsOrganicMediaItemViewMoreOptionsController", "reels"),
)

internal object DirectThreadMediaSaverModuleFingerprint : Fingerprint(
    strings = listOf("DirectThreadMediaSaver"),
    name = "getModuleName",
    returnType = "Ljava/lang/String;",
)

internal object MediaOptionsOverflowMenuCreatorFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("MediaOptionsOverflowMenuCreator"),
)

internal object DirectThreadSaveMessageMediaFingerprint : Fingerprint(
    strings = listOf("saveMessageMedia"),
)
