package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint

/**
 * Exact Instagram 445 anchors derived from the supplied base.apk.
 * These are the integration points used by the downloader port; they are
 * kept separate from the implementation until the required extension/data
 * layer is wired in.
 */
internal object MediaOptionsOverflowHelperFingerprint : Fingerprint(
    name = "A09",
    definingClass = "LX/Zxv;",
    parameters = listOf("Lcom/instagram/feed/media/mediaoption/MediaOption\$Option;"),
    returnType = "V",
    strings = listOf("MediaOptionsOverflowHelper"),
)

internal object ReelMoreOptionsFingerprint : Fingerprint(
    name = "A08",
    definingClass = "LX/9Tx;",
    parameters = listOf(
        "Landroid/view/View;",
        "LX/1Pg;",
        "LX/9PM;",
        "Lcom/instagram/feed/media/Media;",
        "LX/9Tx;",
        "Z",
        "Z",
        "Z",
    ),
    returnType = "V",
    strings = listOf("ClipsOrganicMediaItemViewMoreOptionsController"),
)

internal object DirectThreadMediaSaverModuleFingerprint : Fingerprint(
    name = "getModuleName",
    definingClass = "LX/Kj4;",
    returnType = "Ljava/lang/String;",
    strings = listOf("DirectThreadMediaSaver"),
)

internal object MediaOptionsOverflowMenuCreatorFingerprint : Fingerprint(
    name = "<clinit>",
    definingClass = "LX/ZiN;",
    returnType = "V",
    strings = listOf("MediaOptionsOverflowMenuCreator"),
)

internal object DirectThreadSaveMessageMediaFingerprint : Fingerprint(
    name = "saveMessageMedia",
    definingClass = "Lcom/instagram/direct/fragment/DirectThreadFragment;",
)

internal object CopyTextToStringFingerprint : Fingerprint(
    name = "toString",
    definingClass = "LX/Kk3;",
    returnType = "Ljava/lang/String;",
    strings = listOf("CopyText"),
)
