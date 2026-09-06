package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import app.morphe.patcher.util.proxy.mutableTypes.MutableField.Companion.toMutable
import app.morphe.util.registersUsed
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private const val OPTION_CLASS = "Lcom/instagram/feed/media/mediaoption/MediaOption\$Option;"
private const val EXTENSION_CLASS = "Ldev/zehen/myinsta2/extension/FeedButton;"

private object OptionEnumInitialiserFingerprint : Fingerprint(
    definingClass = OPTION_CLASS,
    name = "<clinit>",
)

private object FeedMenuBuilderFingerprint : Fingerprint(
    definingClass = "LX/C1T;",
    name = "A00",
    returnType = "V",
    strings = listOf("TEXT_POST_APP_INACTIVE"),
)

private object FeedOverflowClickFingerprint : Fingerprint(
    definingClass = "LX/Zxv;",
    name = "A09",
    returnType = "V",
    parameters = listOf(OPTION_CLASS),
    strings = listOf("MediaOptionsOverflowHelper"),
)

private fun ReferenceInstruction.methodReference(): MethodReference? = reference as? MethodReference

@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "Adds a feed overflow download action for Instagram 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        OptionEnumInitialiserFingerprint.apply {
            val optionField = classDef.fields.firstOrNull { it.type == OPTION_CLASS }
                ?: throw IllegalStateException("MyInsta2: ${OPTION_CLASS} backing field not found")

            classDef.fields.add(
                optionField.toMutable().also { it.name = "MYINSTA_DOWNLOAD" },
            )

            method.apply {
                val constructorIndex = instructions.indexOfLast { instruction ->
                    if (instruction.opcode != Opcode.INVOKE_DIRECT) return@indexOfLast false
                    val reference = (instruction as? ReferenceInstruction)?.methodReference() ?: return@indexOfLast false
                    reference.definingClass == OPTION_CLASS && reference.name == "<init>"
                }
                if (constructorIndex < 0) {
                    throw IllegalStateException("MyInsta2: ${OPTION_CLASS} constructor call not found")
                }
                if (constructorIndex + 1 >= instructions.size ||
                    getInstruction(constructorIndex + 1).opcode != Opcode.SPUT_OBJECT
                ) {
                    throw IllegalStateException("MyInsta2: ${OPTION_CLASS} constructor is not followed by enum field assignment")
                }
                addInstructions(
                    constructorIndex + 2,
                    """
                    invoke-static {}, ${EXTENSION_CLASS}->downloadOverflowButton()${OPTION_CLASS}
                    move-result-object v0
                    sput-object v0, ${OPTION_CLASS}->MYINSTA_DOWNLOAD:${OPTION_CLASS}
                    """.trimIndent(),
                )

                val valuesIndex = instructions.indexOfLast { instruction ->
                    if (instruction.opcode != Opcode.INVOKE_STATIC) return@indexOfLast false
                    val reference = (instruction as? ReferenceInstruction)?.methodReference() ?: return@indexOfLast false
                    reference.definingClass == OPTION_CLASS &&
                        reference.name == "\$values" &&
                        reference.returnType == "[${OPTION_CLASS.removePrefix("L").removeSuffix(";")};"
                }
                if (valuesIndex < 0) {
                    throw IllegalStateException("MyInsta2: ${OPTION_CLASS} $values values builder not found")
                }
                val moveResult = valuesIndex + 1
                if (moveResult >= instructions.size || getInstruction(moveResult).opcode != Opcode.MOVE_RESULT_OBJECT) {
                    throw IllegalStateException("MyInsta2: ${OPTION_CLASS} $values result register not found")
                }
                val arrayRegister = getInstruction(moveResult).registersUsed.firstOrNull()
                    ?: throw IllegalStateException("MyInsta2: ${OPTION_CLASS} $values result register unavailable")
                addInstructions(
                    valuesIndex,
                    """
                    invoke-static {}, ${EXTENSION_CLASS}->addToMenuOptionArray()[${OPTION_CLASS}
                    move-result-object v$arrayRegister
                    """.trimIndent(),
                )
            }
        }

        FeedMenuBuilderFingerprint.apply {
            method.apply {
                var arrayListRegister = -1
                var checkCastRegister = -1
                var checkCastIndex = -1

                if (getInstruction(0).opcode == Opcode.INVOKE_STATIC) {
                    arrayListRegister = getInstruction(1).registersUsed.firstOrNull() ?: -1
                    checkCastIndex = instructions.indexOfFirst { it.opcode == Opcode.CHECK_CAST }
                    if (checkCastIndex >= 0) checkCastRegister = getInstruction(checkCastIndex).registersUsed.firstOrNull() ?: -1
                } else {
                    val candidates = instructions.filter {
                        it.opcode == Opcode.NEW_INSTANCE &&
                            (it as? ReferenceInstruction)?.reference is TypeReference &&
                            ((it as ReferenceInstruction).reference as TypeReference).type == "Ljava/util/ArrayList;"
                    }
                    for (instruction in candidates) {
                        val index = instruction.location.index
                        if (index + 3 >= instructions.size) continue
                        if (getInstruction(index + 2).opcode == Opcode.IGET_OBJECT &&
                            getInstruction(index + 3).opcode == Opcode.CHECK_CAST) {
                            arrayListRegister = getInstruction(index + 1).registersUsed.firstOrNull() ?: -1
                            checkCastIndex = instructions.indexOf(getInstruction(index + 3))
                            checkCastRegister = getInstruction(checkCastIndex).registersUsed.firstOrNull() ?: -1
                            break
                        }
                    }
                }

                if (arrayListRegister < 0 || checkCastRegister < 0 || checkCastIndex < 0) {
                    throw IllegalStateException("MyInsta2: could not locate feed overflow ArrayList registers")
                }

                addInstructions(
                    checkCastIndex + 1,
                    """
                    invoke-static {v$checkCastRegister,v$arrayListRegister},${EXTENSION_CLASS}->addFeedOverflowButton(Ljava/lang/Object;Ljava/util/ArrayList;)V
                    """.trimIndent(),
                )
            }
        }

        FeedOverflowClickFingerprint.apply {
            method.apply {
                val activityField = classDef.fields.firstOrNull { it.type == "Landroid/app/Activity;" }
                    ?: throw IllegalStateException("MyInsta2: feed overflow Activity field not found")

                val getMediaObjectMethod = classDef.methods.firstOrNull {
                    it.name != "<init>" &&
                        it.returnType != "V" &&
                        (it.returnType.startsWith("L") || it.returnType.startsWith("[")) &&
                        it.parameterTypes.isEmpty() &&
                        it.implementation?.registerCount == 1 &&
                        !it.isStatic
                } ?: throw IllegalStateException("MyInsta2: instance object-returning feed media getter not found")

                val getterDescriptor = "${getMediaObjectMethod.name}()${getMediaObjectMethod.returnType}"

                addInstructionsWithLabels(
                    0,
                    """
                    move-object/from16 v1, p1
                    invoke-static {v1},${EXTENSION_CLASS}->isCustomButtonPressed(${OPTION_CLASS})Z
                    move-result v0
                    if-eqz v0, :myinsta_original

                    move-object/from16 v0, p0
                    iget-object v5, v0, ${activityField}
                    invoke-virtual {v0},${classDef.type}->${getterDescriptor}
                    move-result-object v2

                    invoke-static {v1,v5,v2},${EXTENSION_CLASS}->customButtonOnClick(${OPTION_CLASS}Landroid/content/Context;Ljava/lang/Object;)Z
                    move-result v0
                    return-void
                    """.trimIndent(),
                    ExternalLabel("myinsta_original", getInstruction(0)),
                )
            }
        }
    }
}
