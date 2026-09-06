# Port status

Target: Instagram `445.0.0.45.83` / version code `385111379` / arm64-v8a.

Reference: MyInsta v26.0 based on Instagram `364.0.0.35.86`.

A feature is marked **ported** only when its 445 implementation exists and its fingerprint has been matched against the target bytecode. It is marked **verified** only after runtime testing on the target release.

| Feature | 364 reference | 445 port | Runtime | Notes |
|---|---:|---:|---:|---|
| Ghost Mode — DM seen | yes | ported | pending | Exact 445 `LX/JmB;->A09(...):V` / `mark_thread_seen-` mapping |
| Ghost Mode — story seen | yes | ported | pending | Exact 445 `LX/0hI;->A06(Context,String,Z):V` / `media/seen/?reel=%s&live_vod=0` mapping |
| Ghost Mode — live seen | yes | ported | pending | Exact 445 Tigon `startRequest(LX/3kv;,LX/3kz;,LX/3lr;):LX/8IW;`; URI register/`URI.getHost()` boundary proven; hook remains opt-in pending runtime test |
| Ghost Mode — typing status | yes | ported | pending | Exact 445 `LX/4tv;->A02(...):List` endpoint builder; patch returns empty list |
| Anti-Revoke notification | yes | ported | pending | Exact 445 `LX/72e;->A01(Intent,LX/2ej;):V` / `revoke_notification` mapping |
| Hide Ads | yes | ported | pending | Exact 445 `LX/4jB;->A02(...):Z` / `Is ad pod` mapping |
| Disable Story Auto-Flipping | yes | candidate | pending | `auto_advance` exists in 445, but the discovered `ReelViewerFragment->A0k(Integer):V` is not yet proven to be the advance control boundary |
| Feed Media Downloader | yes | candidate | pending | Exact 445 MediaOption enum/menu/click anchors exist; media extraction remains heuristic and unvalidated |
| Direct Message Media Download | yes | candidate | pending | Exact 445 `LX/Kj4;->A02(...)V` target now replaces the old first-void-method heuristic; runtime media semantics remain unvalidated |
| Copy Comment | yes | candidate | pending | Exact 445 `LX/FZO;->A09(...):ArrayList`, `A19(Vfc):V`, and `LX/GEL;.A0O:String` mappings implemented; patch is bundled opt-in |
| Distraction Free | yes | partial | pending | Ad suppression is ported; suggested-content filtering still only an anchor |
| Copy helpers | yes | partial | pending | Copy Comment now has an exact 445 candidate; bio/messages remain unresolved |
| Avatar zoom | yes | pending | pending | Media viewer changed across releases |
| Follows indicator | yes | pending | pending | Profile row binding needs target mapping |
| Media quality | yes | pending | pending | Viewer/request path needs target mapping |
| Experiments | yes | pending | pending | Keep version-specific and opt-in |
| Developer tools | yes | pending | pending | Separate from normal user features |
| Monet/theme | yes | pending | pending | Resource/UI implementation required |
| OTA | yes | pending | pending | Update mechanism should not be coupled to Instagram release checks |

## Target evidence

The supplied Library artifact was reconstructed as `base.apk` from `base.zip.001` + `base.zip.002`. Its embedded version string is `445.0.0.45.83`, confirming that the available APK is the repository's 445 target rather than 443.

Current exact target mappings include DM seen `LX/JmB;->A09(UserSession, LX/1ew;, String, String, String):V`, story seen `LX/0hI;->A06(Context, String, Z):V`, typing operation builder `LX/4tv;->A02(...):List`, live Tigon dispatch `TigonServiceLayer->startRequest(LX/3kv;,LX/3kz;,LX/3lr;):LX/8IW;`, anti-revoke `LX/72e;->A01(Intent, LX/2ej;):V`, Hide Ads `LX/4jB;->A02(LX/4jB;, LX/9il;, LX/4oh;):Z`, DirectThread saver `LX/Kj4;->A02(...)V`, and Copy Comment `LX/FZO;->A09(...)ArrayList` / `A19(Vfc):V`.

## Current build verification

- Morphe patch project metadata is configured for Instagram 445.
- Target version and version code are pinned.
- Vendor APK/APKM files are excluded from Git.
- CI has previously produced valid `.mpp` artifacts.
- Every newly added 445 hook is kept default-off until runtime validation where required.

## Not claimed yet

No feature is marked **runtime verified** until the resulting Morphe-patched Instagram 445 build is actually exercised. CI proves that the patch bundle compiles and packages correctly; it cannot prove Instagram runtime behavior.
