package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Prevents the live viewer-count heartbeat from being sent. This is the
 * network-side privacy hook needed for anonymous live viewing; it is kept
 * separate from the story-seen hook so either feature can be selected alone.
 */
private object LiveHeartbeatFingerprint : Fingerprint(
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
        LiveHeartbeatFingerprint.classDef.methods
            .filter { it.returnType == "V" && it.parameters.size >= 1 }
            .maxBy { it.parameters.size }
            .addInstructions(0, "return-void")
    }
}
