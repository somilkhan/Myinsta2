package dev.zehen.myinsta2.distractionfree

import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Placeholder for the suggested-content filter.
 *
 * Instagram 445 does not expose a stable, verified fingerprint for the JSON
 * parser used by this feature. The patch is therefore intentionally a no-op
 * until a target-side hook is verified against the actual APK. In particular,
 * it must never attempt fingerprint matching during patch execution because
 * an unresolved optional feature must not abort an otherwise valid bundle.
 */
@Suppress("unused")
val hideSuggestedContentPatch = bytecodePatch(
    name = "Hide suggested content",
    description = "Reserved for a future verified Instagram 445 suggestion-parser hook.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        // Intentionally empty: unresolved optional fingerprints are never run.
    }
}
