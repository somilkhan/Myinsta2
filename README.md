# myinsta2 by zehen

A versioned engineering workspace for porting the MyInsta modification set onto a current stable Instagram Android base.

## Project rules

- Keep the original MyInsta reference build untouched.
- Target stable Instagram builds only unless explicitly changed.
- Do not commit proprietary Instagram APK binaries or extracted proprietary code.
- Track each modification as a discrete, testable port.
- No feature is marked complete until it passes static and runtime regression checks.

## Current baseline

- Source mod: MyInsta v26.0
- Source Instagram base: 364.0.0.35.86
- Target: Instagram 445.0.0.45.83 stable
- Project: myinsta2 by zehen

## Port order

1. Workspace and reproducibility
2. APK/DEX/resource inventory
3. Hook mapping 364 -> 445
4. Settings infrastructure
5. Downloads
6. Ghost Mode
7. Distraction Free / ad filtering
8. Copy and privacy features
9. Avatar zoom / media quality
10. Experiments / developer tools
11. Theme and miscellaneous features
12. Build, signing, static validation and regression testing

## Status

Repository initialized. Binary inputs remain local and are not committed to this repository.
