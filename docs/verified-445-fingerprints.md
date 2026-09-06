# Verified Instagram 445 fingerprints

Target: `com.instagram.android` `445.0.0.45.83` (`base.apk`, arm64-v8a).

These mappings were extracted from the supplied target APK's DEX files. They are **target mappings**, not runtime verification. A feature is only runtime-verified after the resulting Morphe-patched APK is installed and exercised.

| Feature / subsystem | Defining class | Method | Signature / evidence |
|---|---|---|---|
| Ghost Mode — DM seen | `LX/JmB;` | `A09` | `(Lcom/instagram/common/session/UserSession;LX/1ew;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V`, string `mark_thread_seen-` |
| Ghost Mode — story seen | `LX/0hI;` | `A06` | `(Landroid/content/Context;Ljava/lang/String;Z)V`, string `media/seen/?reel=%s&live_vod=0` |
| Ghost Mode — typing | `LX/4tv;` | `A02` | `(... )Ljava/util/List;`, endpoint `direct_v2/threads/%s/toggle_typing_indicator_control/` |
| Ghost Mode — live heartbeat builder | `LX/QyW;` | `A00` | `(Lcom/instagram/common/session/UserSession;Ljava/lang/String;Ljava/lang/String;)LX/7pe;`, endpoint `/live/%s/heartbeat_and_get_viewer_count/`; dispatch boundary not yet mapped |
| Anti-Revoke notification | `LX/72e;` | `A01` | `(Landroid/content/Intent;LX/2ej;)V`, string `revoke_notification` |
| Hide Ads | `LX/4jB;` | `A02` | `(LX/4jB;LX/9il;LX/4oh;)Z`, string `Is ad pod` |
| Download — feed overflow | `LX/Zxv;` | `A09` | `(Lcom/instagram/feed/media/mediaoption/MediaOption$Option;)V`, string `MediaOptionsOverflowHelper` |
| Download — reel overflow | `LX/9Tx;` | `A08` | `(Landroid/view/View;LX/1Pg;LX/9PM;Lcom/instagram/feed/media/Media;LX/9Tx;ZZZ)V`, string `ClipsOrganicMediaItemViewMoreOptionsController` |
| Download — DM saver module | `LX/Kj4;` | `getModuleName` | `()Ljava/lang/String;`, string `DirectThreadMediaSaver` |
| Download — overflow menu class init | `LX/ZiN;` | `<clinit>` | `()V`, string `MediaOptionsOverflowMenuCreator` |
| Copy-text anchor | `LX/Kk3;` | `toString` | `()Ljava/lang/String;`, string `CopyText` |

## Story flipping status

The previous generic `userSession` fingerprint is not treated as verified. The current 445 story-seen mapping above is separate from story auto-advance. The auto-flip control flow still needs a precise timer/advance boundary before it should be claimed as verified.

## Downloader boundary

The target contains the required downloader-related classes and anchors, but a working downloader needs the complete Morphe extension/data path: media extraction, variant selection, carousel handling, storage/download queue, menu injection, and DM media handling. String presence alone is not treated as implementation evidence.
