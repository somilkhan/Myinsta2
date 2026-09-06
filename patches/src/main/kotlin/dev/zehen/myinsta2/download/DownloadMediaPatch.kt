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
import com.android.tools.smali.dexlib2.iface.reference.TypeReference
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private const val OPTION_CLASS = "Lcom/instagram/feed/media/mediaoption/MediaOption\\$Option;"
private const val EXTENSION_CLASS = "Ldev/zehen/myinsta2/extension/FeedButton;"

private object OptionEnumInitialiserFingerprint : Fingerprint(
    definingClass = OPTION_CLASS,
    name = "<clinit>",
)

/** Instagram's post overflow menu builder. The text anchor is intentionally semantic. */
private object FeedMenuBuilderFingerprint : Fingerprint(
    definingClass = "LX/C1T;",
    name = "A00",
    returnType = "V",
    strings = listOf("TEXT_POST_APP_INACTIVE"),
)

/** Same semantic target used by Piko: overflow helper receiving MediaOption$Option. */
private object FeedOverflowClickFingerprint : Fingerprint(
    definingClass = "LX/Zxv;",
    name = "A09",
    returnType = "V",
    parameters = listOf(OPTION_CLASS),
    strings = listOf("MediaOptionsOverflowHelper"),
)

@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "Adds a feed overflow download action for Instagram 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Extend the real MediaOption$Option value array with our custom option.
        OptionEnumInitialiserFingerprint.apply {
            classDef.fields.add(
                classDef.fields.first { it.type == OPTION_CLASS }.toMutable().also {
                    it.name = "MYINSTA_DOWNLOAD"
                },
            )

            method.apply {
                val lastInvokeDirectIndex = instructions.last { it.opcode == Opcode.INVOKE_DIRECT }.location.index
                addInstructions(
                    lastInvokeDirectIndex + 2,
                    """
                    invoke-static {}, $EXTENSION_CLASS->downloadOverflowButton()$OPTION_CLASS
                    move-result-object v0
                    sput-object v0, $OPTION_CLASS->MYINSTA_DOWNLOAD:$OPTION_CLASS
                    """.trimIndent(),
                )

                val lastInvokeStaticIndex = instructions.last { it.opcode == Opcode.INVOKE_STATIC }.location.index
                val arrayInstructionIndex = lastInvokeStaticIndex - 1
                val arrayRegister = getInstruction(arrayInstructionIndex).registersUsed[0]
                addInstructions(
                    lastInvokeStaticIndex - 1,
                    """
                    invoke-static {}, $EXTENSION_CLASS->addToMenuOptionArray()[$OPTION_CLASS
                    move-result-object v$arrayRegister
                    """.trimIndent(),
                )
            }
        }

        // Reuse Instagram's actual button-adder object and ArrayList rather than fabricating a
        // menu implementation. This mirrors the proven Piko overflow-menu architecture.
        FeedMenuBuilderFingerprint.apply {
            method.apply {
                var arrayListRegister = -1
                var checkCastRegister = -1
                var checkCastIndex = -1

                if (getInstruction(0).opcode == Opcode.INVOKE_STATIC) {
                    arrayListRegister = getInstruction(1).registersUsed[0]
                    checkCastIndex = instructions.indexOfFirst { it.opcode == Opcode.CHECK_CAST }
                    if (checkCastIndex >= 0) checkCastRegister = getInstruction(checkCastIndex).registersUsed[0]
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
                            arrayListRegister = getInstruction(index + 1).registersUsed[0]
                            checkCastIndex = instructions.indexOf(getInstruction(index + 3))
                            checkCastRegister = getInstruction(checkCastIndex).registersUsed[0]
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
                    invoke-static {v$checkCastRegister,v$arrayListRegister},$EXTENSION_CLASS->addFeedOverflowButton(Ljava/lang/Object;Ljava/util/ArrayList;)V
                    """.trimIndent(),
                )
            }
        }

        // Resolve the same media object that Instagram itself uses for the overflow handler.
        // Do not attempt to discover media by reflecting over the overflow-helper internals.
        FeedOverflowClickFingerprint.apply {
            method.apply {
                val activityField = classDef.fields.firstOrNull { it.type == "Landroid/app/Activity;" }
                    ?: throw IllegalStateException("MyInsta2: feed overflow Activity field not found")

                val getMediaObjectMethod = classDef.methods.firstOrNull {
                    it.name != "<init>" &&
                        it.returnType != "V" &&
                        it.implementation?.registerCount == 1
                } ?: throw IllegalStateException("MyInsta2: feed media getter not found")

                addInstructionsWithLabels(
                    0,
                    """
                    move-object/from16 v1, p1
                    invoke-static {v1},$EXTENSION_CLASS->isCustomButtonPressed($OPTION_CLASS)Z
                    move-result v0
                    if-eqz v0, :myinsta_original

                    move-object/from16 v0, p0
                    iget-object v5, v0, $activityField
                    invoke-virtual {v0},${getMediaObjectMethod.name}${getMediaObjectMethod.parameterTypes.joinToString("", prefix = "(", postfix = ")")}${getMediaObjectMethod.returnType}
                    move-result-object v2

                    invoke-static {v1,v5,v2},$EXTENSION_CLASS->customButtonOnClick($OPTION_CLASS Landroid/content/Context;Ljava/lang/Object;)Z
                    move-result v0
                    return-void
                    """.trimIndent(),
                    ExternalLabel("myinsta_original", getInstruction(0)),
                )
            }
        }
    }
}
