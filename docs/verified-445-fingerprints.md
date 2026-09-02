# Verified Instagram 445 fingerprints

Target: `com.instagram.android` `445.0.0.45.83` (`base.apk`, arm64-v8a).

These mappings were extracted from the supplied target APK's DEX files. They are **target mappings**, not runtime verification. A feature is only runtime-verified after the resulting Morphe-patched APK is installed and exercised.

| Feature / subsystem | Defining class | Method | Signature / evidence |
|---|---|---|---|
| Ghost Mode — DM seen | `LX/JmB;` | `A09` | `(Lcom/instagram/common/session/UserSession;LX/1ew;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V`, string `mark_thread_seen-` |
| Ghost Mode — typing | `LX/5nq;` | `Geu` | `(LX/2mc;LX/Ovs;LX/ADU;)V`, endpoint `direct_v2/threads/%s/toggle_typing_indicator_control/` |
| Anti-Revoke notification | `LX/72e;` | `A01` | `(Landroid/content/Intent;LX/2ej;)V`, string `revoke_notification` |
| Hide Ads | `LX/4jB;` | `A02` | `(LX/4jB;LX/9il;LX/4oh;)Z`, string `Is ad pod` |
| Download — feed overflow | `LX/Zxv;` | `A09` | `(Lcom/instagram/feed/media/mediaoption/MediaOption$Option;)V`, string `MediaOptionsOverflowHelper` |
| Download — reel overflow | `LX/9Tx;` | `A08` | `(Landroid/view/View;LX/1Pg;LX/9PM;Lcom/instagram/feed/media/Media;LX/9Tx;ZZZ)V`, string `ClipsOrganicMediaItemViewMoreOptionsController` |
| Download — DM saver module | `LX/Kj4;` | `getModuleName` | `()Ljava/lang/String;`, string `DirectThreadMediaSaver` |
| Download — overflow menu class init | `LX/ZiN;` | `<clinit>` | `()V`, string `MediaOptionsOverflowMenuCreator` |
| Copy-text anchor | `LX/Kk3;` | `toString` | `()Ljava/lang/String;`, string `CopyText` |

## Story flipping status

The old/reference `userSession` fingerprint does **not** currently identify a matching method in the 445 `ReelViewerFragment`. The 445 class does contain story-viewer methods and an `auto_advance` telemetry value, but that alone is not sufficient to identify the automatic-flip control flow. The story-flipping hook therefore remains unverified until its actual timer/advance path is mapped.

## Downloader boundary

The target contains the required downloader-related classes and anchors, but a working downloader needs the complete Morphe extension/data path: media extraction, variant selection, carousel handling, storage/download queue, menu injection, and DM media handling. String presence alone is not treated as implementation evidence.
