package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstruction
import app.morphe.util.registersUsed
import com.android.tools.smali.dexlib2.Opcode
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Instagram 445 live-heartbeat network interception. */
private object TigonServiceLayerStartRequestFingerprint : Fingerprint(
    definingClass = "Lcom/instagram/api/tigon/TigonServiceLayer;",
    name = "startRequest",
)

@Suppress("unused")
val viewLiveAnonymouslyPatch = bytecodePatch(
    name = "View Live Anonymously",
    description = "Blocks Instagram's live viewer-count heartbeat request on Instagram 445.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        TigonServiceLayerStartRequestFingerprint.method.apply {
            val firstIfEqzIndex = indexOfFirstInstruction(Opcode.IF_EQZ)
            if (firstIfEqzIndex < 0) {
                throw IllegalStateException("MyInsta2: Tigon startRequest URI guard not found")
            }

            val uriInstruction = instructions.lastOrNull {
                it.opcode == Opcode.IGET_OBJECT && it.location.index < firstIfEqzIndex
            } ?: throw IllegalStateException("MyInsta2: Tigon request URI field read not found")

            val uriRegister = uriInstruction.registersUsed.firstOrNull()
                ?: throw IllegalStateException("MyInsta2: Tigon request URI register not found")

            addInstructions(
                uriInstruction.location.index + 1,
                """
                invoke-static/range { v$uriRegister .. v$uriRegister }, Ldev/zehen/myinsta2/extension/Links;->interceptUri(Ljava/net/URI;)V
                """.trimIndent(),
            )
        }
    }
}
