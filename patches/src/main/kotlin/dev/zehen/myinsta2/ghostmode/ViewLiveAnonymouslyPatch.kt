package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Prevents the live viewer-count heartbeat from being sent. The fingerprint's
 * matched method is patched directly; do not guess a method from the containing
 * class because Instagram 445 contains many unrelated void methods.
 */
private object LiveHeartbeatFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("/live/%s/heartbeat_and_get_viewer_count/"),
)

@Suppress("unused")
val viewLiveAnonymouslyPatch = bytecodePatch(
    name = "Ghost Mode — view live anonymously",
    description = "Blocks Instagram's live viewer heartbeat on Instagram 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        LiveHeartbeatFingerprint.method.addInstructions(0, "return-void")
    }
}
