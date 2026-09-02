# MyInsta UI/UX

The MyInsta experience is part of the port, not an optional afterthought.

## Scope

Preserve the familiar Instagram experience while exposing the MyInsta controls through a coherent settings surface. Do not redesign Instagram's core navigation, feed, reels, stories, profile, or messaging UI unless the reference implementation changes that surface.

## UX principles

- MyInsta controls should feel native to the host Instagram release.
- Settings are grouped by intent rather than by implementation detail.
- Existing Instagram controls remain available.
- Optional modifications are independently toggleable where the reference behavior allows it.
- Defaults should be conservative and reversible.
- Avoid intrusive dialogs, persistent overlays, or new navigation unless required by the reference implementation.
- Experimental and developer controls stay separated from normal-user settings.

## Settings families

- Privacy / Ghost Mode
- Downloads and media actions
- Distraction-free / content filtering
- Copy and text helpers
- Media quality and profile viewing
- Themes and appearance
- Experiments / developer options
- Updates and diagnostics

## Implementation rule

UI behavior must be implemented through Morphe patches and resources. Do not ship a parallel UI framework or duplicate Instagram screens. Match the MyInsta reference behavior first, then adapt styling/resources to the target Instagram release where necessary.

## Verification

A UI item is not considered complete until:

1. It appears in the intended settings location.
2. Its state persists correctly.
3. Its underlying patch is enabled/disabled correctly.
4. It does not remove unrelated Instagram settings.
5. It survives process restart.
