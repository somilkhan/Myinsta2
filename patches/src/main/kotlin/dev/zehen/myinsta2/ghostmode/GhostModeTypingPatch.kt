package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Instagram 445 typing-indicator request boundary.
 *
 * The endpoint is constructed by LX/5nq;->Geu(LX/2mc;,LX/Ovs;,LX/ADU;)V.
 * This mapping is verified directly against the supplied 445.0.0.45.83
 * base.apk: the method contains the exact
 * direct_v2/threads/%s/toggle_typing_indicator_control/ string.
 */
private object TypingIndicatorRequestFingerprint : Fingerprint(
    definingClass = "LX/5nq;",
    name = "Geu",
    returnType = "V",
    parameters = listOf(
        "LX/2mc;",
        "LX/Ovs;",
        "LX/ADU;",
    ),
    strings = listOf("direct_v2/threads/%s/toggle_typing_indicator_control/"),
)

@Suppress("unused")
val ghostModeTypingPatch = bytecodePatch(
    name = "Ghost Mode — typing status",
    description = "Prevents Instagram's typing-indicator request from being dispatched.",
    // This patch is a dependency of the default MyInsta2 aggregate; Morphe
    // executes dependencies when the parent patch executes.
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        val implementation = TypingIndicatorRequestFingerprint.method.implementation
            as? MutableMethodImplementation
            ?: error("Typing-indicator method does not expose a mutable dexlib2 implementation")

        implementation.addInstruction(
            0,
            BuilderInstruction10x(Opcode.RETURN_VOID),
        )
    }
}
