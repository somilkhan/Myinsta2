package dev.zehen.myinsta2.comments

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import app.morphe.util.indexOfFirstInstruction
import com.android.tools.smali.dexlib2.Opcode
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private const val EXTENSION_CLASS = "Ldev/zehen/myinsta2/extension/CommentCopyUtils;"

/** Exact Instagram 445 comment-action menu builder. */
private object AddCommentButtonFingerprint : Fingerprint(
    definingClass = "LX/FZO;",
    name = "A09",
    returnType = "Ljava/util/ArrayList;",
    parameters = listOf("LX/LW1;", "LX/Fbx;", "Ljava/lang/String;", "Ljava/lang/String;", "Z", "Z"),
    strings = listOf("instagram_share_comment_to_story_entrypoint_impression"),
)

/** Exact Instagram 445 comment-button click handler. */
private object CommentButtonOnClickFingerprint : Fingerprint(
    definingClass = "LX/FZO;",
    name = "A19",
    returnType = "V",
    parameters = listOf("LX/Vfc;"),
    strings = listOf("select_comment_screen_delete_comments_tap", "comment_share_click"),
)

@Suppress("unused")
val copyCommentPatch = bytecodePatch(
    name = "Copy Comment",
    description = "Adds a guarded Copy action to Instagram 445 comment actions.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        AddCommentButtonFingerprint.method.apply {
            val arrayListInit = instructions.firstOrNull { instruction ->
                if (instruction.opcode != Opcode.NEW_INSTANCE) return@firstOrNull false
                val reference = instruction.reference?.toString() ?: return@firstOrNull false
                if (reference != "Ljava/util/ArrayList;") return@firstOrNull false
                val index = instruction.location.index
                if (index + 5 >= instructions.size) return@firstOrNull false
                instructions[index + 1].opcode == Opcode.INVOKE_DIRECT &&
                    instructions[index + 2].opcode == Opcode.MOVE_RESULT_PSEUDO
                    .not()
            }

            if (arrayListInit == null) {
                throw IllegalStateException("MyInsta2: exact 445 comment action ArrayList not found")
            }

            val index = arrayListInit.location.index
            val addObjectIndex = instructions.indexOfFirst { instruction ->
                instruction.location.index > index + 1 &&
                    instruction.opcode == Opcode.IGET_OBJECT &&
                    instruction.location.index <= index + 8
            }
            if (addObjectIndex < 0) {
                throw IllegalStateException("MyInsta2: exact 445 comment object field read not found")
            }

            val arrayRegister = arrayListInit.registersUsed.firstOrNull()
                ?: throw IllegalStateException("MyInsta2: comment action ArrayList register not found")
            val commentRegister = getInstruction(addObjectIndex).registersUsed.getOrNull(1)
                ?: throw IllegalStateException("MyInsta2: comment object register not found")

            addInstruction(
                addObjectIndex + 1,
                """
                invoke-static {v$arrayRegister,v$commentRegister},${EXTENSION_CLASS}->addButton(Ljava/util/List;Ljava/lang/Object;)V
                """.trimIndent(),
            )
        }

        CommentButtonOnClickFingerprint.method.apply {
            val firstIfEqzIndex = indexOfFirstInstruction(Opcode.IF_EQZ)
            if (firstIfEqzIndex < 0) {
                throw IllegalStateException("MyInsta2: exact 445 comment click guard not found")
            }

            val arrayListResult = instructions.lastOrNull {
                it.location.index < firstIfEqzIndex && it.opcode == Opcode.MOVE_RESULT_OBJECT
            } ?: throw IllegalStateException("MyInsta2: exact 445 comment action list result not found")
            val arrayListRegister = arrayListResult.registersUsed.firstOrNull()
                ?: throw IllegalStateException("MyInsta2: comment action list register not found")

            addInstructionsWithLabels(
                arrayListResult.location.index + 1,
                """
                move-object/from16 v0, p1
                invoke-static {v0,v$arrayListRegister},${EXTENSION_CLASS}->checkOnCommentButtonClick(Ljava/lang/Object;Ljava/util/List;)Z
                move-result v0
                if-eqz v0, :myinsta_comment_original
                return-void
                """.trimIndent(),
                ExternalLabel("myinsta_comment_original", getInstruction(arrayListResult.location.index + 1)),
            )
        }
    }
}
