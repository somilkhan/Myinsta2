# Port status

Target: Instagram `445.0.0.45.83` / version code `385111379` / arm64-v8a.

Reference: MyInsta v26.0 based on Instagram `364.0.0.35.86`.

A feature is marked **ported** only when its 445 implementation exists and the patch builds against the target. It is marked **verified** only after runtime testing.

| Feature | 364 reference | 445 port | Runtime | Notes |
|---|---:|---:|---:|---|
| Ghost Mode | yes | pending | pending | Requires fresh 445 fingerprints |
| Downloads | yes | pending | pending | Posts/reels/media paths need target mapping |
| Distraction Free | yes | pending | pending | Feed/reels/stories/explore paths need target mapping |
| Anti-Revoke | yes | pending | pending | Requires current DM event path |
| Copy helpers | yes | pending | pending | Bio/comments/messages are separate hooks |
| Avatar zoom | yes | pending | pending | Media viewer changed across releases |
| Follows indicator | yes | pending | pending | Profile row binding needs target mapping |
| Media quality | yes | pending | pending | Viewer/request path needs target mapping |
| Experiments | yes | pending | pending | Keep version-specific and opt-in |
| Developer tools | yes | pending | pending | Separate from normal user features |
| Monet/theme | yes | pending | pending | Resource/UI implementation required |
| OTA | yes | pending | pending | Update mechanism should not be coupled to Instagram release checks |

## Current verified work

- Morphe patch project metadata is configured for Instagram 445.
- Target version and version code are pinned.
- Vendor APK/APKM files are excluded from Git.
- CI builds an `.mpp` bundle without requiring a local Gradle installation.

## Not claimed yet

No MyInsta feature is currently marked ported or verified. The source APK contains the reference implementation, but its implementation must be mapped to the 445 bytecode before it is safe to ship.
