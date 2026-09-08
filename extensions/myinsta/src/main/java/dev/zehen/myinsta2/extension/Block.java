package dev.zehen.myinsta2.extension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Runtime filter used by the Instagram 445 suggested-content parser hook. */
public final class Block {
    private static final String INVALID_KEY = "myinsta2_invalid_feed_item";

    private static final Set<String> SUGGESTED_CONTENT_KEYS = new HashSet<>(Arrays.asList(
        "clips_netego",
        "stories_netego",
        "in_feed_survey",
        "bloks_netego",
        "suggested_igd_channels",
        "suggested_top_accounts",
        "suggested_users"
    ));

    private Block() {}

    /**
     * Returns an invalid FeedItem type for known suggested-content units.
     * Instagram's existing unknown-type handling then skips the unit without
     * disturbing normal feed-item parsing.
     */
    public static String replaceJsonParserKey(String key) {
        return key != null && SUGGESTED_CONTENT_KEYS.contains(key) ? INVALID_KEY : key;
    }
}
