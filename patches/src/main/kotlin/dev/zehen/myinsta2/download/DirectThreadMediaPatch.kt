package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21t
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22c
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private const val MESSAGE_UTILS = "Ldev/zehen/myinsta2/extension/MessageUtils;"

/** Exact Instagram 445 DirectThreadMediaSaver task-entry method. */
private object DirectThreadMediaSaverTaskFingerprint : Fingerprint(
    definingClass = "LX/Kj4;",
    name = "A07",
    returnType = "V",
    parameters = listOf(
        "LX/XKO;",
        "Lcom/instagram/feed/media/Media;",
        "LX/6ar;",
        "Ljava/lang/String;",
        "Lkotlin/jvm/functions/Function1;",
        "I",
        "Z",
        "Z",
    ),
)

@Suppress("unused")
val directThreadMediaPatch = bytecodePatch(
    name = "Download Direct Messages",
    description = "Adds a guarded media-download path to Instagram 445 direct-message media saving.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        DirectThreadMediaSaverTaskFingerprint.apply {
            val activityField = classDef.fields.firstOrNull { it.type == "Landroid/app/Activity;" }
                ?: throw IllegalStateException("MyInsta2: DirectThreadMediaSaver Activity field not found")
            val implementation = method.implementation as? MutableMethodImplementation
                ?: throw IllegalStateException("MyInsta2: DirectThreadMediaSaver method has no mutable implementation")
            val originalLabel = implementation.newLabelForIndex(0)
            val parameterBase = implementation.registerCount - (method.parameterTypes.size + 1)
            if (parameterBase < 0 || parameterBase + 2 >= implementation.registerCount) {
                throw IllegalStateException("MyInsta2: invalid DirectThreadMediaSaver parameter register layout")
            }
            val p0 = parameterBase
            val p2 = parameterBase + 2
            val activityRef = ImmutableFieldReference(classDef.type, activityField.name, activityField.type)
            val checkRef = ImmutableMethodReference(
                MESSAGE_UTILS,
                "messageDownloadCheck",
                listOf("Landroid/content/Context;", "Ljava/lang/Object;"),
                "Z",
            )

            val instructions = listOf(
                BuilderInstruction22c(Opcode.IGET_OBJECT, 0, p0, activityRef),
                BuilderInstruction22x(Opcode.MOVE_OBJECT, 1, p2),
                BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, 0, 1, 0, 0, 0, checkRef),
                BuilderInstruction11x(Opcode.MOVE_RESULT, 1),
                BuilderInstruction21t(Opcode.IF_EQZ, 1, originalLabel),
                BuilderInstruction10x(Opcode.RETURN_VOID),
            )
            instructions.forEachIndexed { offset, instruction ->
                implementation.addInstruction(offset, instruction)
            }
        }
    }
}
