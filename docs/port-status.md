# Port status

Target: Instagram `445.0.0.45.83` / version code `385111379` / arm64-v8a.

Reference: MyInsta v26.0 based on Instagram `364.0.0.35.86`.

A feature is marked **ported** only when its 445 implementation exists and its fingerprint has been matched against the target bytecode. It is marked **verified** only after runtime testing on the target release.

| Feature | 364 reference | 445 port | Runtime | Notes |
|---|---:|---:|---:|---|
| Ghost Mode — DM seen | yes | ported | pending | `mark_thread_seen-` fingerprint matches the supplied 445 APK; runtime confirmation still required |
| Ghost Mode — story seen | yes | pending | pending | Current 445 story-seen path needs a safe target mapping |
| Ghost Mode — live seen | yes | pending | pending | Current 445 live heartbeat path needs a safe target mapping |
| Ghost Mode — typing status | yes | ported | pending | 445 `direct_v2/threads/%s/toggle_typing_indicator_control/` request path mapped to `LX/5nq;->Geu(...)V` |
| Anti-Revoke notification | yes | ported | pending | 445 `message_revoked` / `revoke_notification` receiver path mapped; runtime confirmation still required |
| Hide Ads | yes | ported | pending | `Is ad pod` fingerprint matches the supplied 445 APK and returns `Z` |
| Disable Story Auto-Flipping | yes | ported | pending | `ReelViewerFragment` timeout action fingerprint matches the supplied 445 APK |
| Downloads | yes | pending | pending | Posts/reels/media paths need target mapping |
| Distraction Free | yes | partial | pending | Ad suppression and story auto-flip controls are ported; feed/reels/stories/explore paths still need target mapping |
| Copy helpers | yes | pending | pending | Bio/comments/messages are separate hooks |
| Avatar zoom | yes | pending | pending | Media viewer changed across releases |
| Follows indicator | yes | pending | pending | Profile row binding needs target mapping |
| Media quality | yes | pending | pending | Viewer/request path needs target mapping |
| Experiments | yes | pending | pending | Keep version-specific and opt-in |
| Developer tools | yes | pending | pending | Separate from normal user features |
| Monet/theme | yes | pending | pending | Resource/UI implementation required |
| OTA | yes | pending | pending | Update mechanism should not be coupled to Instagram release checks |

## Target evidence

The supplied Library artifact was reconstructed as `base.apk` from `base.zip.001` + `base.zip.002`. Its embedded version string is `445.0.0.45.83`, confirming that the available APK is the repository's 445 target rather than 443.

The target bytecode was inspected directly. Current target evidence includes the DM seen hook `LX/JmB;->A09(UserSession, LX/1ew;, String, String, String):V`, the typing-indicator request path `LX/5nq;->Geu(LX/2mc;, LX/Ovs;, LX/ADU;):V`, the anti-revoke notification receiver `LX/72e;->A00(Context, Intent, LX/2ej;, UserSession, LX/72e;, Function0):V`, and the Hide Ads hook `LX/4jB;->A02(LX/4jB;, LX/9il;, LX/4oh;):Z`. These are bytecode matches against the supplied APK, not assumptions based only on version numbers.

## Current build verification

- Morphe patch project metadata is configured for Instagram 445.
- Target version and version code are pinned.
- Vendor APK/APKM files are excluded from Git.
- Previous CI successfully produced the `.mpp` bundle.
- Ghost Mode DM, Ghost Mode typing, Anti-Revoke notification, Hide Ads, and Story Auto-Flipping now have explicit 445 patch implementations.

## Not claimed yet

No feature is marked **runtime verified** until the resulting Morphe-patched Instagram 445 build is actually exercised. Runtime testing requires installing/running the patched APK; CI alone cannot prove Instagram behavior.
