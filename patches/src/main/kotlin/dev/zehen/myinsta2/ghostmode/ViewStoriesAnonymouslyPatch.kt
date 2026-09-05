package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstruction
import app.morphe.util.registersUsed
import com.android.tools.smali.dexlib2.Opcode
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Instagram's story-seen URI builder. The final boolean controls whether the
 * seen operation is emitted. For a selected patch, force that value off.
 */
private object StorySeenUriBuilderFingerprint : Fingerprint(
    strings = listOf("media/seen/?reel=%s&live_vod=0"),
)

@Suppress("unused")
val viewStoriesAnonymouslyPatch = bytecodePatch(
    name = "Ghost Mode — view stories anonymously",
    description = "Prevents the story-seen request from being emitted on Instagram 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        StorySeenUriBuilderFingerprint.classDef.methods
            .last { it.returnType == "Z" }
            .apply {
                val lastIfEqzIndex = instructions.last { it.opcode == Opcode.IF_EQZ }.location.index
                val returnIndex = indexOfFirstInstruction(lastIfEqzIndex, Opcode.RETURN)
                val resultRegister = getInstruction(returnIndex).registersUsed[0]

                addInstructions(
                    returnIndex,
                    "const/4 v$resultRegister, 0x0",
                )
            }
    }
}
