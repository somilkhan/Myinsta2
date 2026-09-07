package dev.zehen.myinsta2.comments

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21t
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private const val EXTENSION_CLASS = "Ldev/zehen/myinsta2/extension/CommentCopyUtils;"

private object AddCommentButtonFingerprint : Fingerprint(
    definingClass = "LX/FZO;",
    name = "A09",
    returnType = "Ljava/util/ArrayList;",
    parameters = listOf("LX/LW1;", "LX/Fbx;", "Ljava/lang/String;", "Ljava/lang/String;", "Z", "Z"),
    strings = listOf("instagram_share_comment_to_story_entrypoint_impression"),
)

private object CommentButtonOnClickFingerprint : Fingerprint(
    definingClass = "LX/FZO;",
    name = "A19",
    returnType = "V",
    parameters = listOf("LX/Vfc;"),
    strings = listOf("select_comment_screen_delete_comments_tap", "comment_share_click"),
)

private fun methodRef(name: String, parameters: List<String>, returnType: String) =
    ImmutableMethodReference(EXTENSION_CLASS, name, parameters, returnType)

@Suppress("unused")
val copyCommentPatch = bytecodePatch(
    name = "Copy Comment",
    description = "Adds a guarded Copy action to Instagram 445 comment actions.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        AddCommentButtonFingerprint.method.apply {
            val implementation = implementation as? MutableMethodImplementation
                ?: throw IllegalStateException("MyInsta2: comment action method has no mutable implementation")
            val methodInstructions = implementation.instructions.toList()
            var arrayListIndex = -1
            for (index in 0 until methodInstructions.size - 2) {
                if (methodInstructions[index].opcode != Opcode.NEW_INSTANCE) continue
                val type = (methodInstructions[index] as? ReferenceInstruction)?.reference as? TypeReference ?: continue
                if (type.type != "Ljava/util/ArrayList;") continue
                if (methodInstructions[index + 1].opcode != Opcode.INVOKE_DIRECT) continue
                val field = (methodInstructions[index + 2] as? ReferenceInstruction)?.reference as? FieldReference ?: continue
                if (methodInstructions[index + 2].opcode == Opcode.IGET_OBJECT &&
                    field.definingClass == "LX/GEL;" && field.name == "A0C" &&
                    field.type == "Lcom/instagram/user/model/User;") {
                    arrayListIndex = index
                    break
                }
            }
            if (arrayListIndex < 0) throw IllegalStateException("MyInsta2: exact 445 comment action ArrayList not found")
            val arrayRegister = (methodInstructions[arrayListIndex] as? OneRegisterInstruction)?.registerA
                ?: throw IllegalStateException("MyInsta2: comment action ArrayList register not found")
            val commentRegister = (methodInstructions[arrayListIndex + 2] as? TwoRegisterInstruction)?.registerB
                ?: throw IllegalStateException("MyInsta2: comment object register not found")
            implementation.addInstruction(
                arrayListIndex + 3,
                BuilderInstruction35c(
                    Opcode.INVOKE_STATIC, 2, arrayRegister, commentRegister, 0, 0, 0,
                    methodRef("addButton", listOf("Ljava/util/List;", "Ljava/lang/Object;"), "V"),
                ),
            )
        }

        CommentButtonOnClickFingerprint.method.apply {
            val implementation = implementation as? MutableMethodImplementation
                ?: throw IllegalStateException("MyInsta2: comment click method has no mutable implementation")
            val methodInstructions = implementation.instructions.toList()
            val firstIfEqzIndex = methodInstructions.indexOfFirst { it.opcode == Opcode.IF_EQZ }
            if (firstIfEqzIndex < 0) throw IllegalStateException("MyInsta2: exact 445 comment click guard not found")
            val resultIndex = methodInstructions.subList(0, firstIfEqzIndex)
                .indexOfLast { it.opcode == Opcode.MOVE_RESULT_OBJECT }
            if (resultIndex < 0) throw IllegalStateException("MyInsta2: exact 445 comment action list result not found")
            val absoluteResultIndex = resultIndex
            val arrayListRegister = (methodInstructions[absoluteResultIndex] as? OneRegisterInstruction)?.registerA
                ?: throw IllegalStateException("MyInsta2: comment action list register not found")
            if (absoluteResultIndex + 1 >= methodInstructions.size) {
                throw IllegalStateException("MyInsta2: comment click original target not found")
            }
            val originalLabel = implementation.newLabelForIndex(absoluteResultIndex + 1)
            val parameterBase = implementation.registerCount - 2
            if (parameterBase < 0 || parameterBase + 1 >= implementation.registerCount) {
                throw IllegalStateException("MyInsta2: invalid comment click parameter register layout")
            }
            val p1 = parameterBase + 1
            val checkRef = methodRef("checkOnCommentButtonClick", listOf("Ljava/lang/Object;", "Ljava/util/List;"), "Z")
            val instructions = listOf(
                BuilderInstruction22x(Opcode.MOVE_OBJECT_FROM16, 0, p1),
                BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, 0, arrayListRegister, 0, 0, 0, checkRef),
                BuilderInstruction11x(Opcode.MOVE_RESULT, 0),
                BuilderInstruction21t(Opcode.IF_EQZ, 0, originalLabel),
                BuilderInstruction10x(Opcode.RETURN_VOID),
            )
            instructions.forEachIndexed { offset, instruction ->
                implementation.addInstruction(absoluteResultIndex + 1 + offset, instruction)
            }
        }
    }
}
