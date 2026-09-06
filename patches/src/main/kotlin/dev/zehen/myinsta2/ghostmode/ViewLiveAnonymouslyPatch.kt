package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.registersUsed
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact Instagram 445 Tigon request entry point. */
private object TigonServiceLayerStartRequestFingerprint : Fingerprint(
    definingClass = "Lcom/instagram/api/tigon/TigonServiceLayer;",
    name = "startRequest",
    returnType = "LX/8IW;",
    parameters = listOf("LX/3kv;", "LX/3kz;", "LX/3lr;"),
)

@Suppress("unused")
val viewLiveAnonymouslyPatch = bytecodePatch(
    name = "View Live Anonymously",
    description = "Blocks Instagram's live viewer-count heartbeat request on Instagram 445; opt-in until runtime validation.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        TigonServiceLayerStartRequestFingerprint.method.apply {
            val uriInstruction = instructions.firstOrNull { instruction ->
                if (instruction.opcode != Opcode.IGET_OBJECT) return@firstOrNull false
                val registers = instruction.registersUsed
                if (registers.size < 2 || registers[0] != 1 || registers[1] != 13) return@firstOrNull false

                val nextIndex = instruction.location.index + 1
                if (nextIndex >= instructions.size) return@firstOrNull false
                val next = instructions[nextIndex]
                if (next.opcode != Opcode.INVOKE_VIRTUAL) return@firstOrNull false

                val reference = (next as? ReferenceInstruction)?.reference as? MethodReference
                    ?: return@firstOrNull false
                reference.definingClass == "Ljava/net/URI;" &&
                    reference.name == "getHost" &&
                    reference.returnType == "Ljava/lang/String;" &&
                    reference.parameterTypes.isEmpty() &&
                    next.registersUsed.firstOrNull() == 1
            } ?: throw IllegalStateException("MyInsta2: exact 445 Tigon URI field read not found")

            addInstructions(
                uriInstruction.location.index + 1,
                """
                invoke-static {v1}, Ldev/zehen/myinsta2/extension/Links;->interceptUri(Ljava/net/URI;)V
                """.trimIndent(),
            )
        }
    }
}
