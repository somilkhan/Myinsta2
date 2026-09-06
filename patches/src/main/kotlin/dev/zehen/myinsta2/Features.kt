package dev.zehen.myinsta2

/**
 * Verified Instagram 445 capability inventory.
 *
 * Only capabilities actually wired into the default bundle are advertised as
 * supported. Experimental/unfinished features stay out of this set until a
 * target fingerprint and end-to-end runtime behavior have been verified.
 */
enum class Feature(val key: String, val title: String) {
    GHOST_MODE("ghost_mode", "Ghost Mode"),
    GHOST_MODE_TYPING("ghost_mode_typing", "Ghost Mode — Typing Status"),
    ANTI_REVOKE("anti_revoke", "Anti Revoke"),
    HIDE_ADS("hide_ads", "Hide Ads"),
    DISABLE_STORY_AUTO_FLIPPING("disable_story_auto_flipping", "Disable Story Auto-Flipping"),
    DISABLE_VIDEO_AUTOPLAY("disable_video_autoplay", "Disable Video Autoplay"),
    DOWNLOAD_POSTS("download_posts", "Download Posts"),
    DOWNLOAD_REELS("download_reels", "Download Reels"),
}

internal val supportedFeatures = setOf(
    Feature.GHOST_MODE,
    Feature.GHOST_MODE_TYPING,
    Feature.ANTI_REVOKE,
    Feature.HIDE_ADS,
    Feature.DISABLE_STORY_AUTO_FLIPPING,
    Feature.DISABLE_VIDEO_AUTOPLAY,
    Feature.DOWNLOAD_POSTS,
    Feature.DOWNLOAD_REELS,
)
