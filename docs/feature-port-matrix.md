# MyInsta2 — Instagram 445 port matrix

Target: Instagram 445.0.0.45.83 arm64-v8a.

This matrix intentionally separates APK evidence from an implemented Morphe hook. A string/class hit is not sufficient to mark a feature ported.

| Feature | 445 evidence currently available | Implementation state | Next verification |
|---|---|---|---|
| Ghost Mode — DM seen | `mark_thread_seen-` | Implemented | Patch APK + DM runtime |
| Ghost Mode — typing | `toggle_typing_indicator_control` | Implemented | Patch APK + typing runtime |
| Anti-Revoke notification | `message_revoked`, `revoke_notification` | Implemented | Patch APK + revoke runtime |
| Hide Ads | `Is ad pod` | Implemented | Patch APK + feed/reels runtime |
| Story auto-flip | `ReelViewerFragment`, `userSession` | Implemented | Patch APK + story runtime |
| Download posts | `MediaOptionsOverflowHelper`, `DownloadOptionsBottomSheetFragment` | Fingerprints added; hook required | Confirm method body + overflow integration |
| Download reels | `ClipsOrganicMediaItemViewMoreOptionsController`, `reels` | Fingerprints added; hook required | Confirm method body + overflow integration |
| DM media download | `DirectThreadMediaSaver`, `saveMessageMedia` | Fingerprints added; hook required | Confirm exact method signature + saver path |
| Copy bio | `accounts/set_biography/`, `profile_bio` | Mapping required | Identify profile UI/bio binding |
| Copy comments | — | Mapping required | Identify comment text holder |
| Copy messages | — | Mapping required | Identify message text holder |
| Avatar zoom | — | Mapping required | Identify profile/avatar viewer |
| Follows indicator | — | Mapping required | Identify follow-state binding |
| Media quality | — | Mapping required | Identify media quality request/config |
| Distraction Free | — | Mapping required | Identify feed/recommendation gates |
| Instasmash | — | Mapping required | Recover old behavior and map 445 equivalents |
| Experiments | — | Mapping required | Map current experiment infrastructure |
| Developer tools | — | Mapping required | Map debug/developer surfaces |
| Monet Theme | — | Mapping required | Determine compatibility with 445 resources/theme system |

## Rule

Do not add speculative fingerprints merely to increase the feature count. For each port:

1. Find a 445 class/method/string anchor.
2. Confirm the method signature and surrounding instructions.
3. Implement the smallest stable hook.
4. Build the Morphe bundle.
5. Patch the target APK.
6. Verify install/startup and the feature's runtime path.
7. Only then promote the feature to verified.
