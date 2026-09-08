# Verified Instagram 445 fingerprints

Target: `com.instagram.android` `445.0.0.45.83` (`base.apk`, arm64-v8a).

These mappings were extracted from the supplied target APK's DEX files. They are **target mappings**, not runtime verification. A feature is only runtime-verified after the resulting Morphe-patched APK is installed and exercised.

| Feature / subsystem | Defining class | Method | Signature / evidence |
|---|---|---|---|
| Ghost Mode — DM seen | `LX/JmB;` | `A09` | `(Lcom/instagram/common/session/UserSession;LX/1ew;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V`, string `mark_thread_seen-` |
| Ghost Mode — story seen | `LX/0hI;` | `A06` | `(Landroid/content/Context;Ljava/lang/String;Z)V`, string `media/seen/?reel=%s&live_vod=0` |
| Ghost Mode — typing | `LX/5nq;` | `Geu` | `(LX/2mc;LX/Ovs;LX/ADU;)V`, endpoint `direct_v2/threads/%s/toggle_typing_indicator_control/` |
| Ghost Mode — live heartbeat builder | `LX/QyW;` | `A00` | `(Lcom/instagram/common/session/UserSession;Ljava/lang/String;Ljava/lang/String;)LX/7pe;`, endpoint `/live/%s/heartbeat_and_get_viewer_count/` |
| Live heartbeat network boundary | `Lcom/instagram/api/tigon/TigonServiceLayer;` | `startRequest` | `(LX/3kv;LX/3kz;LX/3lr;)LX/8IW;`; URI is loaded into `v1` from `p1` immediately before `Ljava/net/URI;->getHost()` |
| Anti-Revoke notification | `LX/72e;` | `A01` | `(Landroid/content/Intent;LX/2ej;)V`, string `revoke_notification` |
| Hide Ads | `LX/4jB;` | `A02` | `(LX/4jB;LX/9il;LX/4oh;)Z`, string `Is ad pod` |
| Download — feed overflow click | `LX/Zxv;` | `A09` | `(Lcom/instagram/feed/media/mediaoption/MediaOption$Option;)V`, string `MediaOptionsOverflowHelper` |
| Download — DM saver | `LX/Kj4;` | `A02` | `(LX/XKO;LX/Nqq;LX/Kj4;Ljava/lang/String;Ljava/util/List;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;IZ)V` |
| Download — DM saver anchor | `LX/Kj4;` | `getModuleName` | `()Ljava/lang/String;`, string `DirectThreadMediaSaver` |
| Copy Comment — menu builder | `LX/FZO;` | `A09` | `(LX/LW1;LX/Fbx;Ljava/lang/String;Ljava/lang/String;ZZ)Ljava/util/ArrayList;`, string `instagram_share_comment_to_story_entrypoint_impression` |
| Copy Comment — click handler | `LX/FZO;` | `A19` | `(LX/Vfc;)V`, strings `select_comment_screen_delete_comments_tap` + `comment_share_click` |
| Copy Comment — comment model | `LX/GEL;` | field | `A0O:Ljava/lang/String;` is read immediately before the comment-click guard in `LX/FZO;->A19` |
| Copy-text anchor | `LX/Kk3;` | `toString` | `()Ljava/lang/String;`, string `CopyText` |

## Story flipping status

The 445 target contains `auto_advance` references, including `ReelViewerFragment->A0k(Ljava/lang/Integer;)V`, but this method is currently treated as an analytics/exit-reason candidate rather than a proven auto-advance control boundary. No production hook is claimed until the actual advance/timer control flow is established.

## Downloader boundary

The feed downloader has exact 445 enum/menu/click anchors and a guarded runtime media resolver, but it remains opt-in until a patched 445 runtime validates media extraction and button dispatch. The DM downloader targets the exact 445 `LX/Kj4;->A02(...)V` saver signature; it also remains opt-in pending runtime validation.
