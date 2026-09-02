package dev.zehen.myinsta2.download

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Instagram 445 carries an explicit eligibility flag for third-party media
 * downloads. This patch makes that eligibility path permissive. It does not
 * add a downloader UI by itself; it is a compatibility layer for MyInsta's
 * downloader integrations and is intentionally opt-in.
 */
private object ThirdPartyDownloadEligibilityFingerprint : Fingerprint(
    returnType = "Z",
    strings = listOf("is_clips_downloadable"),
)

@Suppress("unused")
val enableThirdPartyDownloadsPatch = bytecodePatch(
    name = "Enable third-party downloads",
    description = "Allows Instagram 445 media to be exposed to third-party download integrations.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        ThirdPartyDownloadEligibilityFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x1\nreturn v0",
        )
    }
}
