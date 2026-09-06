package dev.zehen.myinsta2

/**
 * Verified Instagram 445 capability inventory.
 *
 * Only production-validated hooks are listed here. Experimental 445 work stays
 * out of this inventory until patch application and runtime behavior are proven.
 */
enum class Feature(val key: String, val title: String) {
    GHOST_MODE("ghost_mode", "Ghost Mode"),
    GHOST_MODE_TYPING("ghost_mode_typing", "Ghost Mode — Typing Status"),
    VIEW_STORIES_ANONYMOUSLY("view_stories_anonymously", "View Stories Anonymously"),
    ANTI_REVOKE("anti_revoke", "Anti Revoke"),
    HIDE_ADS("hide_ads", "Hide Ads"),
    DISABLE_VIDEO_AUTOPLAY("disable_video_autoplay", "Disable Video Autoplay"),
}

internal val supportedFeatures = setOf(
    Feature.GHOST_MODE,
    Feature.GHOST_MODE_TYPING,
    Feature.VIEW_STORIES_ANONYMOUSLY,
    Feature.ANTI_REVOKE,
    Feature.HIDE_ADS,
    Feature.DISABLE_VIDEO_AUTOPLAY,
)
