package dev.zehen.myinsta2.download

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Instagram 445 post/reel downloader.
 *
 * The UI hook uses Instagram's existing overflow-menu builder and click path;
 * the runtime side deliberately reflects the Media object so obfuscated model
 * classes do not become compile-time dependencies.
 */
@Suppress("unused")
val downloadMediaPatch = bytecodePatch(
    name = "Download media",
    description = "Adds a Download action to Instagram 445 post/reel overflow menus.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        FeedOverflowMenuBuilderFingerprint.method.addInstructions(
            0,
            "invoke-static {p0, p1}, Ldev/zehen/myinsta2/download/DownloadRuntime;->addDownloadButton(Ljava/lang/Object;Ljava/util/ArrayList;)V",
        )

        FeedButtonOnClickFingerprint.method.addInstructionsWithLabels(
            0,
            """
            invoke-static {p1}, Ldev/zehen/myinsta2/download/DownloadRuntime;->isDownloadOption(Ljava/lang/Object;)Z
            move-result v0
            if-eqz v0, :myinsta_original
            invoke-static {p0}, Ldev/zehen/myinsta2/download/DownloadRuntime;->downloadCurrentMedia(Ljava/lang/Object;)V
            return-void
            """.trimIndent(),
            ExternalLabel("myinsta_original", FeedButtonOnClickFingerprint.method.getInstruction(0)),
        )
    }
}
