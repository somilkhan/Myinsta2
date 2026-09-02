package dev.zehen.myinsta2.ads

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Instagram 445 ad-pod predicate. */
private object AdPodFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(
        "LX/9il;",
        "LX/4oh;",
    ),
    strings = listOf("Is ad pod"),
)

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide Ads",
    description = "Suppresses the Instagram ad-pod predicate on the 445 target.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        AdPodFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0",
            "return v0",
        )
    }
}
