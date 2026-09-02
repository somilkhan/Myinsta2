package dev.zehen.myinsta2

/**
 * MyInsta feature inventory.
 *
 * Keep this list capability-oriented: a capability is only promoted to a
 * verified patch after its target fingerprint and runtime behavior are tested.
 */
enum class Feature(val key: String, val title: String) {
    GHOST_MODE("ghost_mode", "Ghost Mode"),
    ANTI_REVOKE("anti_revoke", "Anti Revoke"),
    HIDE_ADS("hide_ads", "Hide Ads"),
    DISABLE_STORY_AUTO_FLIPPING("disable_story_auto_flipping", "Disable Story Auto-Flipping"),
    DISTRACTION_FREE("distraction_free", "Distraction Free"),
    DOWNLOAD_POSTS("download_posts", "Download Posts"),
    DOWNLOAD_REELS("download_reels", "Download Reels"),
    AVATAR_ZOOM("avatar_zoom", "Avatar Zoom"),
    COPY_BIO("copy_bio", "Copy Bio"),
    INSTASMASH("instasmash", "Instasmash"),
}

internal val supportedFeatures = Feature.entries.toSet()
