# Instagram target analysis

## Current target

The project currently targets Instagram **445.0.0.45.83**, arm64-v8a.

## Evidence workflow

Run `tools/verify-instagram-target.sh /path/to/base.apk` against the exact APK selected in Morphe before adding or changing a fingerprint.

The verifier checks conservative anchors for the existing privacy patches and for the next feature families (download, DM media, biography/profile). An anchor match is **not** a runtime verification; it only establishes that the relevant vocabulary exists in the target DEX set.

## Porting rule

For every new feature:

1. Identify the current Instagram class/method from the target DEX.
2. Prefer stable semantic fingerprints over raw offsets.
3. Verify the method signature and surrounding instructions before patching.
4. Keep optional hooks non-fatal when Instagram changes the implementation.
5. Build the Morphe bundle.
6. Apply it to the exact target APK.
7. Exercise the feature and record runtime results before changing its status to verified.

## Priority

1. Media download menu for posts/reels/stories.
2. DM media download.
3. Copy comments/messages/bio.
4. Avatar/profile-picture zoom.
5. Remaining Ghost Mode paths.
6. Distraction Free and media-quality controls.
7. Experiment/developer tooling.
8. Theme/Monet and remaining legacy utilities.
