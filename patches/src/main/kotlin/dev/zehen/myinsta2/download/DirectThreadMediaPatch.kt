package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

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
    strings = listOf("DirectThreadMediaSaver"),
)

/**
 * Instagram 445 direct-message media interception.
 *
 * A07 is the exact task-entry method that receives the resolved Media object
 * and subsequently constructs the saver task. Intercepting here avoids the
 * earlier A02 candidate, whose normal path only consumes the AtomicInteger
 * and list state before entering the task pipeline.
 */
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

            method.apply {
                addInstructionsWithLabels(
                    0,
                    """
                    iget-object v0, p0, $activityField
                    move-object v1, p2
                    invoke-static {v0,v1},Ldev/zehen/myinsta2/extension/MessageUtils;->messageDownloadCheck(Landroid/content/Context;Ljava/lang/Object;)Z
                    move-result v1
                    if-eqz v1, :myinsta_dm_original
                    return-void
                    """.trimIndent(),
                    ExternalLabel("myinsta_dm_original", getInstruction(0)),
                )
            }
        }
    }
}
