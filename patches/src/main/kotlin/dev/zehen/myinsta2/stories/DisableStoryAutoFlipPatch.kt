package dev.zehen.myinsta2.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x

/**
 * Instagram 445 story-timeout callback.
 *
 * Stripped Morphe runtimes do not contain BytecodeUtilsKt, so this patch
 * mutates the dexlib2 implementation directly rather than using Morphe's
 * bytecode helper extensions.
 */
private object StoryAutoFlipFingerprint : Fingerprint(
    definingClass = "Linstagram/features/stories/fragment/ReelViewerFragment;",
    name = "Fji",
    returnType = "V",
    parameters = listOf("Ljava/lang/Object;"),
    strings = listOf("userSession"),
)

@Suppress("unused")
val disableStoryAutoFlipPatch = bytecodePatch(
    name = "Disable Story Auto-Flipping",
    description = "Disable stories automatically flipping/skipping after the timeout.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        val implementation = StoryAutoFlipFingerprint.method.implementation
            as? MutableMethodImplementation
            ?: error("Story auto-flip method does not expose a mutable dexlib2 implementation")

        implementation.addInstruction(
            0,
            BuilderInstruction10x(Opcode.RETURN_VOID),
        )
    }
}
