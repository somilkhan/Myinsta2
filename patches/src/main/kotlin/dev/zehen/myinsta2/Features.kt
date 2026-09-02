package dev.zehen.myinsta2

/**
 * MyInsta feature inventory.
 *
 * Keep this list capability-oriented: a capability is only promoted to a
 * verified patch after its target fingerprint and runtime behavior are tested.
 */
enum class Feature(val key: String, val title: String) {
    GHOST_MODE("ghost_mode", "Ghost Mode"),
    GHOST_MODE_TYPING("ghost_mode_typing", "Ghost Mode — Typing Status"),
    ANTI_REVOKE("anti_revoke", "Anti Revoke"),
    HIDE_ADS("hide_ads", "Hide Ads"),
    DISABLE_STORY_AUTO_FLIPPING("disable_story_auto_flipping", "Disable Story Auto-Flipping"),
    DISTRACTION_FREE("distraction_free", "Distraction Free"),
    DOWNLOAD_POSTS("download_posts", "Download Posts"),
    DOWNLOAD_REELS("download_reels", "Download Reels"),
    AVATAR_ZOOM("avatar_zoom", "Avatar Zoom"),
    COPY_BIO("copy_bio", "Copy Bio"),
    COPY_COMMENTS("copy_comments", "Copy Comments"),
    COPY_MESSAGES("copy_messages", "Copy Messages"),
    FOLLOWS_INDICATOR("follows_indicator", "Follows You Indicator"),
    IMPROVE_MEDIA_QUALITY("improve_media_quality", "Improve Media Quality"),
    INSTASMASH("instasmash", "Instasmash"),
    EXPERIMENTS("experiments", "Experiment Tools"),
    DEVELOPER_TOOLS("developer_tools", "Developer Tools"),
    MONET_THEME("monet_theme", "Monet Theme"),
}

internal val supportedFeatures = Feature.entries.toSet()
