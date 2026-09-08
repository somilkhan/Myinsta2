package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableField.Companion.toMutable
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21t
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22c
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private const val OPTION_CLASS = "Lcom/instagram/feed/media/mediaoption/MediaOption\$Option;"
private const val EXTENSION_CLASS = "Ldev/zehen/myinsta2/extension/FeedButton;"
private const val MEDIA_CLASS = "Lcom/instagram/feed/media/Media;"

private object OptionEnumInitialiserFingerprint : Fingerprint(
    definingClass = OPTION_CLASS,
    name = "<clinit>",
)

private object FeedOverflowClickFingerprint : Fingerprint(
    definingClass = "LX/Zxv;",
    name = "A09",
    returnType = "V",
    parameters = listOf(OPTION_CLASS),
    strings = listOf("MediaOptionsOverflowHelper"),
)

private fun ReferenceInstruction.methodReference(): MethodReference? = reference as? MethodReference

private fun com.android.tools.smali.dexlib2.iface.instruction.Instruction.outputRegister(): Int? =
    (this as? OneRegisterInstruction)?.registerA

private fun methodRef(
    definingClass: String,
    name: String,
    parameters: List<String>,
    returnType: String,
) = ImmutableMethodReference(definingClass, name, parameters, returnType)

private fun isArrayListNewInstance(instruction: com.android.tools.smali.dexlib2.iface.instruction.Instruction): Boolean =
    instruction.opcode == Opcode.NEW_INSTANCE &&
        (instruction as? ReferenceInstruction)?.reference?.toString() == "Ljava/util/ArrayList;"

private fun findFeedMenuBuilderClass(): String {
    // Instagram 445.0.0.45.83 no longer keeps TEXT_POST_APP_INACTIVE inside
    // the same method that builds the feed overflow list. Do not fingerprint
    // that string: it makes the patch fail before we can inspect the actual
    // ArrayList/check-cast builder shape.
    val candidates = classes.flatMap { classDef ->
        classDef.methods.mapNotNull { method ->
            val implementation = method.implementation ?: return@mapNotNull null
            val instructions = implementation.instructions.toList()
            if (method.returnType != "V") return@mapNotNull null

            var score = 0
            var hasBuilderShape = false
            for (index in 0 until instructions.size - 3) {
                if (!isArrayListNewInstance(instructions[index])) continue
                if (instructions[index + 1].opcode != Opcode.INVOKE_DIRECT) continue
                if (instructions[index + 2].opcode != Opcode.IGET_OBJECT) continue
                if (instructions[index + 3].opcode != Opcode.CHECK_CAST) continue
                hasBuilderShape = true
                score = maxOf(score, 10)
            }
            if (!hasBuilderShape) return@mapNotNull null

            // Prefer methods that still carry the old stable string somewhere
            // in their implementation, but do not require it.
            if (instructions.any { instruction ->
                    (instruction as? ReferenceInstruction)?.reference?.toString() == "TEXT_POST_APP_INACTIVE"
                }) score += 100

            Triple(classDef.type, method, score)
        }
    }

    val bestScore = candidates.maxOfOrNull { it.third }
        ?: throw IllegalStateException("MyInsta2: Instagram 445 feed overflow builder method not found")
    val best = candidates.filter { it.third == bestScore }
    if (best.size != 1) {
        throw IllegalStateException("MyInsta2: Instagram 445 feed overflow builder is ambiguous (${best.size} candidates)")
    }
    return best.single().first
}

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

            classDef.fields.add(optionField.toMutable().also { it.name = "MYINSTA_DOWNLOAD" })

            method.apply {
                val implementation = implementation as? MutableMethodImplementation
                    ?: throw IllegalStateException("MyInsta2: ${OPTION_CLASS} initializer is not mutable")

                val constructorIndex = implementation.instructions.indexOfLast { instruction ->
                    if (instruction.opcode != Opcode.INVOKE_DIRECT) return@indexOfLast false
                    val reference = (instruction as? ReferenceInstruction)?.methodReference() ?: return@indexOfLast false
                    reference.definingClass == OPTION_CLASS && reference.name == "<init>"
                }
                if (constructorIndex < 0) throw IllegalStateException("MyInsta2: ${OPTION_CLASS} constructor call not found")
                if (constructorIndex + 1 >= implementation.instructions.size || implementation.instructions[constructorIndex + 1].opcode != Opcode.SPUT_OBJECT) {
                    throw IllegalStateException("MyInsta2: ${OPTION_CLASS} constructor is not followed by enum field assignment")
                }

                val optionFieldRef = ImmutableFieldReference(OPTION_CLASS, "MYINSTA_DOWNLOAD", OPTION_CLASS)
                val downloadButtonRef = methodRef(EXTENSION_CLASS, "downloadOverflowButton", emptyList(), OPTION_CLASS)
                implementation.addInstruction(constructorIndex + 2, BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0, downloadButtonRef))
                implementation.addInstruction(constructorIndex + 3, BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0))
                implementation.addInstruction(constructorIndex + 4, BuilderInstruction21c(Opcode.SPUT_OBJECT, 0, optionFieldRef))

                val valuesIndex = implementation.instructions.indexOfLast { instruction ->
                    if (instruction.opcode != Opcode.INVOKE_STATIC) return@indexOfLast false
                    val reference = (instruction as? ReferenceInstruction)?.methodReference() ?: return@indexOfLast false
                    reference.definingClass == OPTION_CLASS && reference.name == "\$values" && reference.returnType == "[${OPTION_CLASS}"
                }
                if (valuesIndex < 0) throw IllegalStateException("MyInsta2: ${OPTION_CLASS} \$values values builder not found")
                val moveResult = valuesIndex + 1
                if (moveResult >= implementation.instructions.size || implementation.instructions[moveResult].opcode != Opcode.MOVE_RESULT_OBJECT) {
                    throw IllegalStateException("MyInsta2: ${OPTION_CLASS} \$values result register not found")
                }
                val arrayRegister = implementation.instructions[moveResult].outputRegister()
                    ?: throw IllegalStateException("MyInsta2: ${OPTION_CLASS} \$values result register unavailable")
                val addArrayRef = methodRef(EXTENSION_CLASS, "addToMenuOptionArray", emptyList(), "[${OPTION_CLASS}")
                implementation.addInstruction(valuesIndex, BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0, addArrayRef))
                implementation.addInstruction(valuesIndex + 1, BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, arrayRegister))
            }
        }

        val feedMenuBuilderClass = findFeedMenuBuilderClass()
        val feedMenuBuilder = classBy { it.type == feedMenuBuilderClass }?.classDef
            ?: throw IllegalStateException("MyInsta2: Instagram 445 feed overflow builder class is not mutable")
        val builderMethod = feedMenuBuilder.methods.firstOrNull { method ->
            method.returnType == "V" && method.implementation?.instructions?.any { instruction ->
                instruction.opcode == Opcode.NEW_INSTANCE &&
                    (instruction as? ReferenceInstruction)?.reference?.toString() == "Ljava/util/ArrayList;"
            } == true
        } ?: throw IllegalStateException("MyInsta2: Instagram 445 feed overflow builder method disappeared")

        builderMethod.apply {
            val implementation = implementation as? MutableMethodImplementation
                ?: throw IllegalStateException("MyInsta2: feed menu builder is not mutable")
            var arrayListRegister = -1
            var checkCastRegister = -1
            var checkCastIndex = -1

            for (index in 0 until implementation.instructions.size - 3) {
                if (!isArrayListNewInstance(implementation.instructions[index])) continue
                if (implementation.instructions[index + 1].opcode != Opcode.INVOKE_DIRECT) continue
                if (implementation.instructions[index + 2].opcode != Opcode.IGET_OBJECT) continue
                if (implementation.instructions[index + 3].opcode != Opcode.CHECK_CAST) continue
                arrayListRegister = implementation.instructions[index].outputRegister() ?: -1
                checkCastIndex = index + 3
                checkCastRegister = implementation.instructions[checkCastIndex].outputRegister() ?: -1
                if (arrayListRegister >= 0 && checkCastRegister >= 0) break
            }

            if (arrayListRegister < 0 || checkCastRegister < 0 || checkCastIndex < 0) {
                throw IllegalStateException("MyInsta2: could not locate feed overflow ArrayList registers")
            }
            if (arrayListRegister > 15 || checkCastRegister > 15) {
                throw IllegalStateException("MyInsta2: feed overflow builder uses registers above v15; range invoke required")
            }

            val addButtonRef = methodRef(EXTENSION_CLASS, "addFeedOverflowButton", listOf("Ljava/lang/Object;", "Ljava/util/ArrayList;"), "V")
            implementation.addInstruction(
                checkCastIndex + 1,
                BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, checkCastRegister, arrayListRegister, 0, 0, 0, addButtonRef),
            )
        }

        FeedOverflowClickFingerprint.apply {
            method.apply {
                val implementation = implementation as? MutableMethodImplementation
                    ?: throw IllegalStateException("MyInsta2: feed overflow click handler is not mutable")
                val activityField = classDef.fields.firstOrNull { it.type == "Landroid/app/Activity;" }
                    ?: throw IllegalStateException("MyInsta2: feed overflow Activity field not found")
                val getter = classDef.methods.firstOrNull {
                    it.name == "A01" && it.returnType == MEDIA_CLASS && it.parameterTypes == listOf(classDef.type) && it.implementation != null
                } ?: throw IllegalStateException("MyInsta2: exact 445 feed media getter LX/Zxv;->A01 not found")

                val parameterBase = implementation.registerCount - (method.parameterTypes.size + 1)
                if (parameterBase < 0) throw IllegalStateException("MyInsta2: invalid feed overflow parameter register layout")
                val p0 = parameterBase
                val p1 = parameterBase + 1
                val originalLabel = implementation.newLabelForIndex(0)

                val optionClassRef = methodRef(EXTENSION_CLASS, "isCustomButtonPressed", listOf(OPTION_CLASS), "Z")
                val clickRef = methodRef(EXTENSION_CLASS, "customButtonOnClick", listOf(OPTION_CLASS, "Landroid/content/Context;", "Ljava/lang/Object;"), "Z")
                val getterRef = methodRef(
                    getter.definingClass,
                    getter.name,
                    getter.parameterTypes.map(CharSequence::toString),
                    getter.returnType.toString(),
                )
                val activityRef = ImmutableFieldReference(classDef.type, activityField.name, activityField.type)

                implementation.addInstruction(0, BuilderInstruction22x(Opcode.MOVE_OBJECT_FROM16, 1, p1))
                implementation.addInstruction(1, BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0, optionClassRef))
                implementation.addInstruction(2, BuilderInstruction11x(Opcode.MOVE_RESULT, 0))
                implementation.addInstruction(3, BuilderInstruction21t(Opcode.IF_EQZ, 0, originalLabel))
                implementation.addInstruction(4, BuilderInstruction22x(Opcode.MOVE_OBJECT_FROM16, 0, p0))
                implementation.addInstruction(5, BuilderInstruction22c(Opcode.IGET_OBJECT, 5, 0, activityRef))
                implementation.addInstruction(6, BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0, getterRef))
                implementation.addInstruction(7, BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2))
                implementation.addInstruction(8, BuilderInstruction35c(Opcode.INVOKE_STATIC, 3, 1, 5, 2, 0, 0, clickRef))
                implementation.addInstruction(9, BuilderInstruction11x(Opcode.MOVE_RESULT, 0))
                implementation.addInstruction(10, BuilderInstruction21t(Opcode.IF_EQZ, 0, originalLabel))
                implementation.addInstruction(11, BuilderInstruction10x(Opcode.RETURN_VOID))
            }
        }
    }
}
