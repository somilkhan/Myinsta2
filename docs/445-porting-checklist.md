# Instagram 445 porting checklist

Target: Instagram 445.0.0.45.83 (arm64-v8a).

## Verified bytecode mappings
- DM seen suppression: `LX/JmB;->A09(...)V` / `mark_thread_seen`
- Typing suppression: `LX/5nq;->Geu(LX/2mc;LX/Ovs;LX/ADU;)V` / `toggle_typing_indicator_control`
- Ad suppression: `LX/4jB;->A02(LX/4jB;LX/9il;LX/4oh;)Z` / `Is ad pod`
- Story auto-flip hook: `ReelViewerFragment->Fji(Ljava/lang/Object;)V` / `userSession`
- Revoke notification path: `LX/72e;->A00(...)V` / `message_revoked`

## Porting rule
A feature is not marked port-complete unless its 445 fingerprint is found in the supplied APK and the patch has a safe failure mode when the fingerprint is absent.

## Remaining feature families
- media download/save
- copy caption/comment/bio
- avatar zoom
- follows-you indicator
- media quality controls
- distraction-free controls
- experiments/developer controls
- theme/Monet integration
