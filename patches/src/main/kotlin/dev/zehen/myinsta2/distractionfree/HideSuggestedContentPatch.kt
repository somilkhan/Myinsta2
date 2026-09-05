package dev.zehen.myinsta2.distractionfree

import app.morphe.library.instagram.utility.replaceJsonFieldWithBogus
import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private val SUGGESTED_CONTENT_KEYS = arrayOf(
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

private object FeedItemParseFromJsonFingerprint : Fingerprint(
    name = "unsafeParseFromJson",
    strings = SUGGESTED_CONTENT_KEYS.toList(),
)

private context(_: BytecodePatchContext)
fun hideSuggestedContent() {
    SUGGESTED_CONTENT_KEYS.forEach { key ->
        replaceJsonFieldWithBogus(key)
    }
}

@Suppress("unused")
val hideSuggestedContentPatch = bytecodePatch(
    name = "Hide suggested content",
    description = "Filters suggested feed, reel and story recommendation items on Instagram 445.",
    default = false,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        FeedItemParseFromJsonFingerprint.method.hideSuggestedContent()
    }
}
