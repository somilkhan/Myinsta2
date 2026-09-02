package dev.zehen.myinsta2.distractionfree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Removes the ad-pod decision from the feed controller.
 *
 * The fingerprint is deliberately anchored to the stable user-visible string
 * used by Instagram's ad-pod predicate rather than an obfuscated class name.
 */
private object IsAdPodFingerprint : Fingerprint(
    strings = listOf("Is ad pod"),
)

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide ads",
    description = "Prevents Instagram's feed controller from treating an item as an ad pod.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        IsAdPodFingerprint.method.returnEarly(false)
    }
}
