package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint

/**
 * Exact Instagram 445 anchors derived from the supplied base.apk.
 * These anchors identify the integration points; the executable downloader
 * layer is kept separate until its extension/data dependencies are wired.
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
        "Landroid/view/View;", "LX/1Pg;", "LX/9PM;",
        "Lcom/instagram/feed/media/Media;", "LX/9Tx;", "Z", "Z", "Z",
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

/** `saveMessageMedia` is a target-side log/action label, not a stable method name. */
internal object DirectThreadSaveMessageMediaFingerprint : Fingerprint(
    strings = listOf("DirectThreadFragment.saveMessageMedia"),
)

internal object CopyTextToStringFingerprint : Fingerprint(
    name = "toString",
    definingClass = "LX/Kk3;",
    returnType = "Ljava/lang/String;",
    strings = listOf("CopyText"),
)
