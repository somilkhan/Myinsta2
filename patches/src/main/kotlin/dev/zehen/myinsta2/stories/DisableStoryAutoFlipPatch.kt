package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact 445 story-viewer timeout/advance callback anchor. */
private object StoryAutoFlipFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;"),
    strings = listOf("auto_advance"),
    custom = { method, _ ->
        method.definingClass == "Linstagram/features/stories/fragment/ReelViewerFragment;"
    },
)

@Suppress("unused")
val disableStoryAutoFlipPatch = bytecodePatch(
    name = "Disable Story Auto-Flipping",
    description = "Prevents the automatic story advance callback on Instagram 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        StoryAutoFlipFingerprint.method.addInstructions(0, "return-void")
    }
}
