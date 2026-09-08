# MyInsta2 — Instagram 445 port matrix

Target: Instagram 445.0.0.45.83 arm64-v8a.

This matrix intentionally separates APK evidence from an implemented Morphe hook. A string/class hit is not sufficient to mark a feature ported.

| Feature | 445 evidence currently available | Implementation state | Next verification |
|---|---|---|---|
| Ghost Mode — DM seen | `LX/JmB;->A09(...)V`, `mark_thread_seen-` | Implemented | Patch APK + DM runtime |
| Ghost Mode — story seen | `LX/0hI;->A04(UserSession):LX/7po;`, `media/seen/?reel=%s&live_vod=0` | Implemented | Patch APK + story runtime |
| Ghost Mode — typing | `LX/5nq;->Geu(...)V`, `toggle_typing_indicator_control` | Implemented | Patch APK + typing runtime |
| Anti-Revoke notification | `LX/72e;->A01(Intent, LX/2ej;):V`, `revoke_notification` | Implemented | Patch APK + revoke runtime |
| Hide Ads | `LX/4jB;->A02(...):Z`, `Is ad pod` | Implemented | Patch APK + feed/reels runtime |
| Story auto-flip | `ReelViewerFragment->Fji(Object):V`, `userSession` | Implemented | Patch APK + story runtime |
| Disable video autoplay | `LX/13A;->A00(UserSession):Z`, autoplay setting strings | Implemented | Patch APK + feed/reels runtime |
| Download posts | `MediaOptionsOverflowHelper`, `DownloadOptionsBottomSheetFragment` | Candidate only | Confirm exact 445 executable hook and UI integration |
| Download reels | `ClipsOrganicMediaItemViewMoreOptionsController`, reels anchors | Candidate only | Confirm exact 445 executable hook and UI integration |
| DM media download | `LX/Kj4;->A07(...):V`, `DirectThreadMediaSaver` | Candidate only | Verify register layout, media URL path, patch application |
| Copy bio | `accounts/set_biography/`, `profile_bio` | Mapping required | Identify profile UI/bio binding |
| Copy comments | `LX/FZO;->A09(...):ArrayList`, `A19(Vfc):V` | Candidate only | Confirm runtime semantics and UI injection |
| Copy messages | — | Mapping required | Identify message text holder |
| Avatar zoom | — | Mapping required | Identify profile/avatar viewer |
| Follows indicator | — | Mapping required | Identify follow-state binding |
| Media quality | — | Mapping required | Identify media quality request/config |
| Distraction Free | — | Mapping required | Identify feed/recommendation gates |
| Instasmash | — | Mapping required | Recover old behavior and map 445 equivalents |
| Experiments | — | Mapping required | Map current experiment infrastructure |
| Developer tools | — | Mapping required | Map debug/developer surfaces |
| Monet Theme | — | Mapping required | Determine compatibility with 445 resources/theme system |
| OTA | — | Mapping required | Design release/update mechanism independently of Instagram hooks |

## Promotion rule

Do not add speculative fingerprints merely to increase the feature count. For each port:

1. Find a 445 class/method/string anchor.
2. Confirm the method signature and surrounding instructions.
3. Implement the smallest stable hook.
4. Build the Morphe bundle.
5. Patch the target APK.
6. Verify install/startup and the feature's runtime path.
7. Only then promote the feature to verified.
