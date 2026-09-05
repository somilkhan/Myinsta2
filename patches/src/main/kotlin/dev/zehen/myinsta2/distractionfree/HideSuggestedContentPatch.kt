package dev.zehen.myinsta2.distractionfree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

/**
 * Exact 445 suggestion-parser anchor. The implementation is intentionally
 * disabled until the target-side JSON utility is available in the patch
 * library; keeping the fingerprint here documents the verified target mapping
 * without breaking the Morphe bundle build.
 */
private val SUGGESTED_CONTENT_KEYS = listOf(
    "clips_netego",
    "stories_netego",
    "in_feed_survey",
    "bloks_netego",
    "suggested_igd_channels",
    "suggested_top_accounts",
    "suggested_users",
    "suggested_businesses",
    "suggested_hashtags",
    "suggested_producers_v2",
    "suggested_producers",
    "suggested_close_friends",
    "suggested_shops",
)

@Suppress("unused")
private object FeedItemParseFromJsonFingerprint : Fingerprint(
    name = "unsafeParseFromJson",
    strings = SUGGESTED_CONTENT_KEYS,
)

@Suppress("unused")
val hideSuggestedContentPatch = bytecodePatch(
    name = "Hide suggested content",
    description = "445 suggestion parser anchor; executable filtering is pending a target-side JSON hook.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        FeedItemParseFromJsonFingerprint.method
    }
}
