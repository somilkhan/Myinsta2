package dev.zehen.myinsta2.comments

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference
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
            // Do not use Morphe's BytecodeUtilsKt instruction helpers here.
            // Morphe Manager's stripped runtime does not ship that helper class.
            val methodInstructions = implementation?.instructions?.toList()
                ?: throw IllegalStateException("MyInsta2: comment action method has no implementation")

            var arrayListIndex = -1
            for (index in 0 until methodInstructions.size - 2) {
                val instruction = methodInstructions[index]
                if (instruction.opcode != Opcode.NEW_INSTANCE) continue
                val reference = (instruction as? ReferenceInstruction)?.reference as? TypeReference
                    ?: continue
                if (reference.type != "Ljava/util/ArrayList;") continue
                if (methodInstructions[index + 1].opcode != Opcode.INVOKE_DIRECT) continue
                val fieldInstruction = methodInstructions[index + 2]
                if (fieldInstruction.opcode != Opcode.IGET_OBJECT) continue
                val fieldReference = (fieldInstruction as? ReferenceInstruction)?.reference as? FieldReference
                    ?: continue
                if (fieldReference.definingClass == "LX/GEL;" &&
                    fieldReference.name == "A0C" &&
                    fieldReference.type == "Lcom/instagram/user/model/User;") {
                    arrayListIndex = index
                    break
                }
            }

            if (arrayListIndex < 0) {
                throw IllegalStateException("MyInsta2: exact 445 comment action ArrayList not found")
            }

            val arrayListInit = methodInstructions[arrayListIndex]
            val commentFieldInstruction = methodInstructions[arrayListIndex + 2]
            val arrayRegister = (arrayListInit as? OneRegisterInstruction)?.registerA
                ?: throw IllegalStateException("MyInsta2: comment action ArrayList register not found")
            val commentRegister = (commentFieldInstruction as? TwoRegisterInstruction)?.registerB
                ?: throw IllegalStateException("MyInsta2: comment object register not found")

            addInstructions(
                arrayListIndex + 3,
                """
                invoke-static {v$arrayRegister,v$commentRegister},${EXTENSION_CLASS}->addButton(Ljava/util/List;Ljava/lang/Object;)V
                """.trimIndent(),
            )
        }

        CommentButtonOnClickFingerprint.method.apply {
            val methodInstructions = implementation?.instructions?.toList()
                ?: throw IllegalStateException("MyInsta2: comment click method has no implementation")

            var firstIfEqzIndex = -1
            for (index in methodInstructions.indices) {
                if (methodInstructions[index].opcode == Opcode.IF_EQZ) {
                    firstIfEqzIndex = index
                    break
                }
            }
            if (firstIfEqzIndex < 0) {
                throw IllegalStateException("MyInsta2: exact 445 comment click guard not found")
            }

            var arrayListResultIndex = -1
            for (index in 0 until firstIfEqzIndex) {
                if (methodInstructions[index].opcode == Opcode.MOVE_RESULT_OBJECT) {
                    arrayListResultIndex = index
                }
            }
            if (arrayListResultIndex < 0) {
                throw IllegalStateException("MyInsta2: exact 445 comment action list result not found")
            }

            val result = methodInstructions[arrayListResultIndex]
            val arrayListRegister = (result as? OneRegisterInstruction)?.registerA
                ?: throw IllegalStateException("MyInsta2: comment action list register not found")

            addInstructionsWithLabels(
                arrayListResultIndex + 1,
                """
                move-object/from16 v0, p1
                invoke-static {v0,v$arrayListRegister},${EXTENSION_CLASS}->checkOnCommentButtonClick(Ljava/lang/Object;Ljava/util/List;)Z
                move-result v0
                if-eqz v0, :myinsta_comment_original
                return-void
                """.trimIndent(),
                ExternalLabel("myinsta_comment_original", methodInstructions[arrayListResultIndex + 1]),
            )
        }
    }
}
