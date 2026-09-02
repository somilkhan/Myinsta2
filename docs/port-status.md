# Port Status

## Baseline

| Item | Value | Status |
|---|---|---|
| MyInsta source | v26.0 / Instagram 364.0.0.35.86 | Reference |
| Target | Instagram 445.0.0.45.83 stable | Target |
| Project | myinsta2 by zehen | Active |

## Feature inventory

| Feature family | Initial status | Notes |
|---|---|---|
| Ghost Mode | MAPPED | Requires target hook mapping |
| Downloads | MAPPED | Posts/reels/custom downloader present in source |
| Distraction Free | MAPPED | Feed/reels/stories/explore/comments controls present |
| Anti-Revoke | MAPPED | Source implementation present |
| Copy features | MAPPED | Bio/comments/messages/clipboard functionality present |
| Avatar zoom | MAPPED | Source implementation present |
| Follows indicator | MAPPED | Source implementation present |
| Photo/media quality | MAPPED | Source implementation present |
| Experiments | MAPPED | Backup/restore/merge implementation present |
| Developer tools | MAPPED | Developer/Instasmash implementation present |
| Monet/theme | MAPPED | Source implementation present |
| OTA | MAPPED | Source implementation present |

## Completion policy

A feature is only marked PORTED after its target hook is identified and the resulting build passes static validation. It is only marked VERIFIED after runtime regression testing.
