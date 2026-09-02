# myinsta2 by zehen

Morphe patches for Instagram, maintained as a versioned patch set rather than a repackaged Instagram distribution.

## Target

- Package: `com.instagram.android`
- Release: `445.0.0.45.83`
- Version code: `385111379`
- Architecture: arm64-v8a
- Minimum Android: 9
- Target SHA-1: `5b6e9ef060d157947ea8507b3f8ee87dc8078ed1`

The target is pinned deliberately. A patch is not considered compatible with another Instagram release until it has been validated against that release.

## What this project ports

The reference implementation is MyInsta v26.0, based on Instagram 364.0.0.35.86. The port is being rebuilt as native Morphe patches so the result can be applied to an untouched Instagram APK/APKM from Morphe Manager.

Feature families include privacy controls, downloads, distraction controls, media utilities, copy helpers, profile/media viewing improvements, experiment tooling, and the MyInsta settings surface.

## Build

```bash
./gradlew buildAndroid
```

The build produces an `.mpp` bundle under `patches/build/libs/`.

Do not commit Instagram APKs/APKM bundles, generated patch indexes, or signing material. Supply the original target APK through Morphe when patching.

## Development rules

- Fingerprints must be anchored to stable method/type evidence, not arbitrary offsets.
- Every Instagram target version is explicitly declared in compatibility metadata.
- A patch is `ported` only after it builds and matches the target bytecode.
- A patch is `verified` only after runtime testing on the target release.
- Failed optional patches must not make the base Instagram app unusable.
- Generated release metadata is maintained by the Morphe release workflow.

## Status

The repository contains the Morphe build foundation and target manifest. Feature ports are tracked in `docs/port-status.md` and are not marked complete until validated.
