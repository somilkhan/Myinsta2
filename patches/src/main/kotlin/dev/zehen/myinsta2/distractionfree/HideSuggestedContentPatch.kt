package dev.zehen.myinsta2.distractionfree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact Instagram 445 FeedItem JSON deserializer. */
private object FeedItemParseFromJsonFingerprint : Fingerprint(
    definingClass = "LX/2xv;",
    name = "unsafeParseFromJson",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("LX/2yL;"),
    strings = listOf("suggested_users"),
)

@Suppress("unused")
val hideSuggestedContentPatch = bytecodePatch(
    name = "Hide suggested content",
    description = "Drops Instagram 445 suggested feed units by invalidating their FeedItem JSON type key.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)
    extendWith("extensions/myinsta.mpe")

    execute {
        val implementation = FeedItemParseFromJsonFingerprint.method.implementation
            ?: error("Instagram 445 FeedItem parser has no implementation")
        val instructions = implementation.instructions

        // In the verified 445 parser, the first CONST_STRING/JUMBO is the
        // suggested_users type token. The immediately preceding MOVE_RESULT_OBJECT
        // contains the parsed JSON type key. Rewrite that value through the
        // runtime helper before Instagram dispatches on the key.
        val firstJumbo = instructions.indexOfFirst { it.opcode == Opcode.CONST_STRING_JUMBO }
        require(firstJumbo > 0) { "Instagram 445 FeedItem parser lost its JSON type anchor" }

        val moveResultIndex = instructions.subList(0, firstJumbo)
            .indexOfLast { it.opcode == Opcode.MOVE_RESULT_OBJECT }
        require(moveResultIndex >= 0) { "Instagram 445 FeedItem parser has no JSON key move-result" }

        val moveResult = instructions[moveResultIndex] as? OneRegisterInstruction
            ?: error("Instagram 445 JSON key move-result has an unexpected instruction format")
        val register = moveResult.registerA

        FeedItemParseFromJsonFingerprint.method.addInstructions(
            moveResultIndex + 1,
            "invoke-static {v$register}, Ldev/zehen/myinsta2/extension/Block;->replaceJsonParserKey(Ljava/lang/String;)Ljava/lang/String;\nmove-result-object v$register",
        )
    }
}
