package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact Instagram 445 DirectThreadMediaSaver save/callback method. */
private object DirectThreadMediaSaverSaveFingerprint : Fingerprint(
    definingClass = "LX/Kj4;",
    name = "A02",
    returnType = "V",
    parameters = listOf(
        "LX/XKO;",
        "LX/Nqq;",
        "LX/Kj4;",
        "Ljava/lang/String;",
        "Ljava/util/List;",
        "Ljava/util/concurrent/atomic/AtomicInteger;",
        "Ljava/util/concurrent/atomic/AtomicInteger;",
        "Ljava/util/concurrent/atomic/AtomicInteger;",
        "Ljava/util/concurrent/atomic/AtomicInteger;",
        "Ljava/util/concurrent/atomic/AtomicInteger;",
        "I",
        "Z",
    ),
    strings = listOf("DirectThreadMediaSaver"),
)

/**
 * Instagram 445 direct-message media interception.
 *
 * The hook targets the exact 445 A02 signature rather than selecting the
 * first void method in the class. In A02, p3 is the DirectThreadMediaSaver
 * instance (which owns the Activity field) and p2 is the message/media
 * candidate passed into the save path.
 */
@Suppress("unused")
val directThreadMediaPatch = bytecodePatch(
    name = "Download Direct Messages",
    description = "Adds a guarded media-download path to Instagram 445 direct-message media saving.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        DirectThreadMediaSaverSaveFingerprint.apply {
            val activityField = classDef.fields.firstOrNull { it.type == "Landroid/app/Activity;" }
                ?: throw IllegalStateException("MyInsta2: DirectThreadMediaSaver Activity field not found")

            method.apply {
                addInstructionsWithLabels(
                    0,
                    """
                    iget-object v0, p3, $activityField
                    move-object v1, p2
                    invoke-static {v0,v1},Ldev/zehen/myinsta2/extension/MessageUtils;->messageDownloadCheck(Landroid/content/Context;Ljava/lang/Object;)Z
                    move-result v1
                    if-nez v1, :myinsta_dm_original
                    return-void
                    """.trimIndent(),
                    ExternalLabel("myinsta_dm_original", getInstruction(0)),
                )
            }
        }
    }
}
