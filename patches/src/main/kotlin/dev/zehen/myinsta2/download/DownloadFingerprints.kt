package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint

/** Exact Instagram 445 feed downloader anchors. */
internal object FeedButtonOnClickFingerprint : Fingerprint(
    name = "A09",
    definingClass = "LX/Zxv;",
    parameters = listOf("Lcom/instagram/feed/media/mediaoption/MediaOption\$Option;"),
    returnType = "V",
    strings = listOf("MediaOptionsOverflowHelper"),
)

/** 445 overflow-menu builder entry point. */
internal object FeedOverflowMenuBuilderFingerprint : Fingerprint(
    name = "A02",
    definingClass = "LX/ZiN;",
    parameters = listOf("LX/ZiN;", "Ljava/util/ArrayList;"),
    returnType = "V",
)

internal object MediaOptionsOverflowMenuCreatorFingerprint : Fingerprint(
    name = "<clinit>",
    definingClass = "LX/ZiN;",
    returnType = "V",
    strings = listOf("MediaOptionsOverflowMenuCreator"),
)

internal object DirectThreadMediaSaverModuleFingerprint : Fingerprint(
    name = "getModuleName",
    definingClass = "LX/Kj4;",
    returnType = "Ljava/lang/String;",
    strings = listOf("DirectThreadMediaSaver"),
)

internal object DirectThreadSaveMessageMediaFingerprint : Fingerprint(
    strings = listOf("DirectThreadFragment.saveMessageMedia"),
)

internal object CopyTextToStringFingerprint : Fingerprint(
    name = "toString",
    definingClass = "LX/Kk3;",
    returnType = "Ljava/lang/String;",
    strings = listOf("CopyText"),
)
