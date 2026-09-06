package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Verified 445 heartbeat request-builder anchor.
 *
 * The endpoint is built by LX/QyW;->A00 and that method returns LX/7pe;.
 * Returning void here would be invalid bytecode, so this patch remains an
 * explicit anchor until the request-dispatch boundary is mapped. It is not
 * bundled into MyInsta2 while it is only an anchor.
 */
private object LiveHeartbeatFingerprint : Fingerprint(
    definingClass = "LX/QyW;",
    name = "A00",
    returnType = "LX/7pe;",
    parameters = listOf(
        "Lcom/instagram/common/session/UserSession;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
    ),
    strings = listOf("/live/%s/heartbeat_and_get_viewer_count/"),
)

@Suppress("unused")
val viewLiveAnonymouslyPatch = bytecodePatch(
    name = "Ghost Mode — view live anonymously (anchor)",
    description = "Verified 445 live-heartbeat request builder anchor; dispatch hook is not yet bundled.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Deliberately no-op: this method returns LX/7pe;, so an injected
        // return-void would corrupt the patched APK. Keep the exact anchor
        // available for the next request-dispatch mapping step.
        LiveHeartbeatFingerprint.method
    }
}
