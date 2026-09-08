# Port status

Target: Instagram `445.0.0.45.83` / version code `385111379` / arm64-v8a.

Reference: MyInsta v26.0 based on Instagram `364.0.0.35.86`.

A feature is marked **ported** when its 445 implementation and target fingerprint are matched. It is marked **verified** only after runtime testing on the target release.

| Feature | 364 reference | 445 port | Runtime | Notes |
|---|---:|---:|---:|---|
| Ghost Mode — DM seen | yes | ported | pending | Exact 445 `LX/JmB;->A09(UserSession, LX/1ew;, String, String, String):V` / `mark_thread_seen-` mapping |
| Ghost Mode — story seen | yes | ported | pending | Exact 445 `LX/0hI;->A04(UserSession):LX/7po;` / `media/seen/?reel=%s&live_vod=0` mapping; implementation returns a type-correct null before request construction |
| Ghost Mode — live seen | yes | candidate | pending | Exact 445 Tigon `startRequest(LX/3kv;,LX/3kz;,LX/3lr;):LX/8IW;`; URI boundary is identified, but this hook remains opt-in and is not part of the production aggregate |
| Ghost Mode — typing status | yes | ported | pending | Exact 445 `LX/5nq;->Geu(LX/2mc;,LX/Ovs;,LX/ADU;):V` containing `direct_v2/threads/%s/toggle_typing_indicator_control/`; patch returns before request construction |
| Anti-Revoke notification | yes | ported | pending | Exact 445 `LX/72e;->A01(Intent, LX/2ej;):V` / `revoke_notification` mapping |
| Hide Ads | yes | ported | pending | Exact 445 `LX/4jB;->A02(LX/4jB;, LX/9il;, LX/4oh;):Z` / `Is ad pod` mapping |
| Disable Story Auto-Flipping | yes | ported | pending | Exact 445 `ReelViewerFragment->Fji(Object):V` / `userSession`; implementation uses a return-early timeout callback |
| Disable Video Autoplay | yes | ported | pending | Exact 445 `LX/13A;->A00(UserSession):Z` with all three autoplay-setting strings |
| Feed Media Downloader | yes | candidate | pending | Earlier 445 menu/getter anchors are present in analysis, but no stable executable feed-download hook has been promoted to production |
| Direct Message Media Download | yes | candidate | pending | Exact 445 `LX/Kj4;->A07(LX/XKO;,Media,LX/6ar;,String,Function1,I,Z,Z):V` saver task entry exists; executable interception still needs verifier/runtime validation |
| Copy Comment | yes | candidate | pending | Exact 445 `LX/FZO;->A09(...):ArrayList`, `A19(Vfc):V`; runtime semantics still unvalidated |
| Distraction Free | yes | partial | pending | Suggested-content parser anchor exists; executable filtering remains intentionally disabled |
| Copy helpers | yes | partial | pending | Copy Comment has an exact 445 candidate; other helpers remain unresolved |
| Avatar zoom | yes | pending | pending | Media viewer changed across releases |
| Follows indicator | yes | pending | pending | Profile row binding needs target mapping |
| Media quality | yes | pending | pending | Viewer/request path needs target mapping |
| Experiments | yes | pending | pending | Keep version-specific and opt-in |
| Developer tools | yes | pending | pending | Separate from normal user features |
| Monet/theme | yes | pending | pending | Resource/UI implementation required |
| OTA | yes | pending | pending | Update mechanism should not be coupled to Instagram release checks |

## Target evidence

The supplied Library artifact was reconstructed as `base.apk` from `base.zip.001` + `base.zip.002`. Its embedded version string is `445.0.0.45.83`, confirming that the available APK is the repository's 445 target rather than 443.

The current exact target mappings were checked against that APK's DEX files for DM seen, story seen, typing, anti-revoke, Hide Ads, story timeout, video autoplay, DirectThread saver task, and Copy Comment. The story-seen mapping specifically uses `LX/0hI;->A04(Lcom/instagram/common/session/UserSession;):LX/7po;`; the older `A06` documentation was incorrect and is no longer used by the production patch.

## Current build verification

- Morphe patch project metadata is configured for Instagram 445.
- Target version and version code are pinned.
- Vendor APK/APKM files are excluded from Git.
- CI produces `.mpp` artifacts and publishes the latest bundle.
- The latest successful build also compiles the opt-in candidate patches, while the production aggregate contains only the validated 445 feature set.
- Stripped-runtime helper dependencies (`FreeRegisterProviderKt`, `registersUsed`, and `BytecodeUtils`) are not used by the patch sources.

## Verification boundary

CI proves that the patch bundle compiles and packages correctly. Static DEX analysis proves that the documented fingerprints exist in the supplied Instagram 445 APK. Neither proves Instagram runtime behavior. Runtime verification still requires applying the resulting bundle to Instagram 445 and exercising each production feature on-device.
