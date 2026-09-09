# Verified Instagram 445 fingerprints

Target: `com.instagram.android` `445.0.0.45.83` (`base.apk`, arm64-v8a).

These mappings were extracted from the supplied target APK (`sha256: 9a8d3b0782fba2872d9a5c2012a32d118d0653151492cc9f370fb2e68de112eb`). They are **target mappings**, not runtime verification. A feature is only runtime-verified after the resulting Morphe-patched APK is installed and exercised.

| Feature / subsystem | Defining class | Method | Signature / evidence |
|---|---|---|---|
| **MyInsta2 settings entry** | `LX/Dyw;` | `A05` | `(LX/AOq;LX/KCa;)V`; contains exactly one `invoke-static` to `LX/gAr;->A04(Landroidx/fragment/app/FragmentActivity;LX/AOq;Lcom/instagram/common/session/UserSession;Ljava/lang/Integer;)V` with four registers `{v1,v5,v12,v12}` |
| Profile action-bar setup | `LX/gAr;` | `A04` | `(Landroidx/fragment/app/FragmentActivity;LX/AOq;Lcom/instagram/common/session/UserSession;Ljava/lang/Integer;)V` |
| Ghost Mode — DM seen | `LX/JmB;` | `A09` | `(Lcom/instagram/common/session/UserSession;LX/1ew;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V`, string `mark_thread_seen-` |
| Ghost Mode — story seen | `LX/0hI;` | `A04` | `(Lcom/instagram/common/session/UserSession;)LX/7po;`, endpoint `media/seen/?reel=%s&live_vod=0` |
| Ghost Mode — typing | `LX/5nq;` | `Geu` | `(LX/2mc;LX/Ovs;LX/ADU;)V`, endpoint `direct_v2/threads/%s/toggle_typing_indicator_control/` |
| Ghost Mode — live heartbeat builder | `LX/QyW;` | `A00` | `(Lcom/instagram/common/session/UserSession;Ljava/lang/String;Ljava/lang/String;)LX/7pe;`, endpoint `/live/%s/heartbeat_and_get_viewer_count/` |
| Live heartbeat network boundary | `Lcom/instagram/api/tigon/TigonServiceLayer;` | `startRequest` | `(LX/3kv;LX/3kz;LX/3lr;)LX/8IW;`; URI is loaded into `v1` before `URI.getHost()` |
| Anti-Revoke notification | `LX/72e;` | `A01` | `(Landroid/content/Intent;LX/2ej;)V`, string `revoke_notification` |
| Hide Ads | `LX/4jB;` | `A02` | `(LX/4jB;LX/9il;LX/4oh;)Z`, string `Is ad pod` |
| Disable video autoplay | `LX/13A;` | `A00` | `(Lcom/instagram/common/session/UserSession;)Z`, strings `ig_olympus_disable_video_autoplay`, `ig_disable_video_autoplay`, `ig_video_setting` |
| Disable story auto-flip | `Linstagram/features/stories/fragment/ReelViewerFragment;` | `Fji` | `(Ljava/lang/Object;)V`, string `userSession` |
| Download — feed overflow click | `LX/Zxv;` | `A09` | `(Lcom/instagram/feed/media/mediaoption/MediaOption$Option;)V`, string `MediaOptionsOverflowHelper` |
| Download — DM saver | `LX/Kj4;` | `A02` | `(LX/XKO;LX/Nqq;LX/Kj4;Ljava/lang/String;Ljava/util/List;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;IZ)V` |
| Copy Comment — menu builder | `LX/FZO;` | `A09` | `(LX/LW1;LX/Fbx;Ljava/lang/String;Ljava/lang/String;ZZ)Ljava/util/ArrayList;`, comment-action anchor |
| Copy Comment — click handler | `LX/FZO;` | `A19` | `(LX/Vfc;)V`, strings `select_comment_screen_delete_comments_tap` + `comment_share_click` |
| Copy-text anchor | `LX/Kk3;` | `toString` | `()Ljava/lang/String;`, string `CopyText` |

## Settings resource evidence

The supplied 445 APK contains resource names including `self_profile_switcher`, `profile_action_bar`, `action_bar_overflow_icon`, `overflow_button`, `overflow_button_right`, `overflow_button_layout`, `more_button`, and `more_button_click_area`. The runtime entry point uses these target-confirmed resource names only after the verified `LX/Dyw.A05` profile-action-bar path invokes it.
