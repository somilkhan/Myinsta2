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

@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "Adds a real Instagram 445 feed overflow download action using a bundled Morphe runtime extension.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // 1) Add a real static field to Instagram's MediaOption$Option class and
        // initialize it from our runtime extension. This follows the proven
        // Morphe/Piko enum-extension mechanism instead of reflectively creating
        // an enum object at runtime.
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

        // 2) Add the new option to the feed overflow menu.
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
                            it.getReference<TypeReference>()?.type == "Ljava/util/ArrayList;"
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

        // 3) Consume the custom option before Instagram's normal click dispatch.
        FeedOverflowClickFingerprint.apply {
            method.apply {
                addInstructionsWithLabels(
                    0,
                    """
                    invoke-static {p1,p0},$EXTENSION_CLASS->handleFeedButton($OPTION_CLASS,Ljava/lang/Object;)Z
                    move-result v0
                    if-eqz v0, :myinsta_original
                    return-void
                    """.trimIndent(),
                    ExternalLabel("myinsta_original", getInstruction(0)),
                )
            }
        }
    }
}
