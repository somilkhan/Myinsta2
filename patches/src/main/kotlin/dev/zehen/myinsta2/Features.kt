package dev.zehen.myinsta2

/**
 * Verified Instagram 445 capability inventory.
 *
 * A feature is listed as supported only when the bundle contains a functional,
 * type-safe target hook. Discovery anchors and unverified UI work are kept out
 * of this inventory so the project never claims functionality that is not
 * actually present in the generated patch.
 */
enum class Feature(val key: String, val title: String) {
    GHOST_MODE("ghost_mode", "Ghost Mode"),
    GHOST_MODE_TYPING("ghost_mode_typing", "Ghost Mode — Typing Status"),
    VIEW_STORIES_ANONYMOUSLY("view_stories_anonymously", "View Stories Anonymously"),
    VIEW_LIVE_ANONYMOUSLY("view_live_anonymously", "View Live Anonymously"),
    ANTI_REVOKE("anti_revoke", "Anti Revoke"),
    HIDE_ADS("hide_ads", "Hide Ads"),
    DISABLE_VIDEO_AUTOPLAY("disable_video_autoplay", "Disable Video Autoplay"),
}

internal val supportedFeatures = setOf(
    Feature.GHOST_MODE,
    Feature.GHOST_MODE_TYPING,
    Feature.VIEW_STORIES_ANONYMOUSLY,
    Feature.VIEW_LIVE_ANONYMOUSLY,
    Feature.ANTI_REVOKE,
    Feature.HIDE_ADS,
    Feature.DISABLE_VIDEO_AUTOPLAY,
)
