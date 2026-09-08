# MyInsta2

Morphe patches for Instagram, maintained as a versioned patch set rather than a repackaged Instagram distribution.

## Morphe Manager source

Add this repository once as a **Remote Patch Source** in Morphe Manager:

`https://github.com/somilkhan/Myinsta2`

Morphe will read the generated `patches-bundle.json` and follow each versioned release automatically. New releases use semantic version tags and versioned `.mpp` assets, so you do **not** need to manually download the MPP after every update.

<!-- PATCHES_START -->
> **[v1.0.3](https://github.com/somilkhan/Myinsta2/releases/tag/v1.0.3)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;14 patches total

| Patch | Description | Options |
|---|---|---|
| [Anti Revoke — notification](#anti-revoke-notification) | Suppresses Instagram's revoked-message notification action on 445. |  |
| [Copy Comment](#copy-comment) | Adds a guarded Copy action to Instagram 445 comment actions. |  |
| [Disable Story Auto-Flipping](#disable-story-auto-flipping) | Disable stories automatically flipping/skipping after the timeout. |  |
| [Disable video autoplay](#disable-video-autoplay) | Forces the Instagram 445 video-autoplay preference predicate off. |  |
| [Download Direct Messages](#download-direct-messages) | Adds a guarded media-download path to Instagram 445 direct-message media saving. |  |
| [Download media](#download-media) | Adds a feed overflow download action for Instagram 445. |  |
| [Ghost Mode — DM seen](#ghost-mode-dm-seen) | Blocks Instagram's direct-message seen event on 445. |  |
| [Ghost Mode — typing status](#ghost-mode-typing-status) | Prevents Instagram's typing-indicator request from being dispatched. |  |
| [Ghost Mode — view stories anonymously](#ghost-mode-view-stories-anonymously) | Prevents Instagram 445 from constructing the story-seen request. |  |
| [Hide Ads](#hide-ads) | Suppresses the Instagram ad-pod predicate on the 445 target. |  |
| [Hide suggested content](#hide-suggested-content) | Drops Instagram 445 suggested feed units by invalidating their FeedItem JSON type key. |  |
| [MyInsta2](#myinsta2) | Validated MyInsta2 features for Instagram 445. |  |
| [MyInsta2 — Changelog + Settings](#myinsta2-changelog-settings) | Shows the first-launch/update changelog and exposes the MyInsta profile long-press settings surface. |  |
| [View Live Anonymously](#view-live-anonymously) | Blocks Instagram's live viewer-count heartbeat request on Instagram 445; opt-in until runtime validation. |  |
<!-- PATCHES_END -->

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

The MyInsta2 first-launch/update surface shows the installed Instagram version dynamically, release changes, developer attribution, verified credits, and clickable Telegram/support contacts.

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
