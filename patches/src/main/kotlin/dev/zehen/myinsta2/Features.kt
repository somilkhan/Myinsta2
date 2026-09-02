package dev.zehen.myinsta2

/**
 * MyInsta feature inventory.
 *
 * These are kept as capabilities until a target-specific bytecode implementation
 * is verified against the current Instagram build. A capability is deliberately
 * not exposed as a working patch until its fingerprint is proven on-device.
 */
enum class Feature(val key: String, val title: String) {
    GHOST_MODE("ghost_mode", "Ghost Mode"),
    ANTI_REVOKE("anti_revoke", "Anti Revoke"),
    DISTRACTION_FREE("distraction_free", "Distraction Free"),
    DOWNLOAD_POSTS("download_posts", "Download Posts"),
    DOWNLOAD_REELS("download_reels", "Download Reels"),
    AVATAR_ZOOM("avatar_zoom", "Avatar Zoom"),
    COPY_BIO("copy_bio", "Copy Bio"),
    INSTASMASH("instasmash", "Instasmash"),
}

internal val supportedFeatures = Feature.entries.toSet()
