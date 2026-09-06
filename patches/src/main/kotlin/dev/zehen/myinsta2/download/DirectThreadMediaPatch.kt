package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/** Exact 445 DirectThreadMediaSaver module-name anchor. */
private object DirectThreadMediaSaverModuleNameFingerprint : Fingerprint(
    definingClass = "LX/Kj4;",
    name = "getModuleName",
    returnType = "Ljava/lang/String;",
    strings = listOf("DirectThreadMediaSaver"),
)

/**
 * Instagram 445 direct-message media interception.
 *
 * Kept opt-in until a patched 445 runtime confirms the saver method's p1/p2
 * semantics. The target fingerprint itself is exact and is backed by the
 * uploaded 445 APK's LX/Kj4 + getModuleName + DirectThreadMediaSaver symbols.
 */
@Suppress("unused")
val directThreadMediaPatch = bytecodePatch(
    name = "Download Direct Messages",
    description = "Adds a guarded media-download path to Instagram 445 direct-message media saving.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        DirectThreadMediaSaverModuleNameFingerprint.apply {
            val activityField = classDef.fields.firstOrNull { it.type == "Landroid/app/Activity;" }
                ?: throw IllegalStateException("MyInsta2: DirectThreadMediaSaver Activity field not found")

            val saverMethod = classDef.methods.firstOrNull {
                it.name != "<init>" &&
                    it.returnType == "V" &&
                    it.parameterTypes.size >= 2
            } ?: throw IllegalStateException("MyInsta2: DirectThreadMediaSaver save method not found")

            saverMethod.apply {
                addInstructionsWithLabels(
                    0,
                    """
                    iget-object v0, p1, $activityField
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
