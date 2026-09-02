# Implementation policy

MyInsta2 targets Instagram 445.0.0.45.83 first.

A feature is not considered ported from the old MyInsta APK merely because its name exists in `Features.kt` or because a related string appears in the target APK.

For every feature we require:

1. A target-specific class/method fingerprint.
2. A safe Morphe hook with the correct method signature.
3. Successful bundle compilation.
4. Successful patch application to the target APK.
5. Startup verification of the patched APK.
6. Runtime verification of the feature where the environment permits it.

If method-level evidence is unavailable, leave the feature unmapped rather than shipping a speculative hook.

## Compatibility

The supplied `base.apk` is the primary target. Other Instagram versions may be added only after their fingerprints are independently verified.

## Failure handling

A failed fingerprint match must not be converted into a broad or weak matcher just to make a build pass. When a target changes, update the fingerprint and retest the affected feature.
