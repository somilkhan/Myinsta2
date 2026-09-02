# Target compatibility

MyInsta2 targets the stable Instagram release recorded in the project README.

Patches must use fingerprints and compatibility declarations rather than raw
instruction indexes. A patch that cannot identify its target safely is not
enabled merely to make a build pass.

## Runtime loop

1. Patch the target APKM with Morphe.
2. Install the resulting build.
3. Exercise the affected feature.
4. Capture the Morphe patch log or crash/stack trace when a fingerprint misses.
5. Update the affected fingerprint or implementation.
6. Rebuild and repeat.

A successful compilation is not sufficient evidence of compatibility.
