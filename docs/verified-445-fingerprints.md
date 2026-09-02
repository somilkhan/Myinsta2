# Verified Instagram 445 fingerprints

Target: `com.instagram.android` `445.0.0.45.83` (`base.apk`, arm64-v8a).

These mappings were cross-checked against the supplied target APK's DEX files. A feature is still not considered runtime-verified until the resulting Morphe-patched APK is installed and exercised.

| Feature | Defining class | Method | Signature / evidence |
|---|---|---|---|
| Ghost Mode — DM seen | `LX/JmB;` | `A09` | `(Lcom/instagram/common/session/UserSession;LX/1ew;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V`, string `mark_thread_seen-` |
| Ghost Mode — typing | `LX/5nq;` | `Geu` | `(LX/2mc;LX/Ovs;LX/ADU;)V`, string `direct_v2/threads/%s/toggle_typing_indicator_control/` |
| Anti-Revoke notification | `LX/72e;` | `A00` | `(Landroid/content/Context;Landroid/content/Intent;LX/2ej;Lcom/instagram/common/session/UserSession;LX/72e;Lkotlin/jvm/functions/Function0;)V`, strings `message_revoked` / `revoke_notification` |
| Hide Ads | `LX/4jB;` | `A02` | `(LX/4jB;LX/9il;LX/4oh;)Z`, string `Is ad pod` |
| Disable Story Auto-Flipping | `Linstagram/features/stories/fragment/ReelViewerFragment;` | `Fji` | `(Ljava/lang/Object;)V`, string `userSession` |

## Important boundary

The DEX also contains many `download_url`, `permalink`, and clipboard-related symbols. Those symbols alone are insufficient evidence for a complete MyInsta downloader or copy feature because they are used by multiple unrelated Instagram data/model paths. Do not turn these into patches without identifying the actual UI/event method and its control flow.

The next downloader implementation should therefore be based on a complete Morphe extension/integration path (or a newly identified 445 UI hook), not a speculative `return` patch against a model field.
