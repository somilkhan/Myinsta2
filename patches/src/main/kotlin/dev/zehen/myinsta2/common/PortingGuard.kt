package dev.zehen.myinsta2.common

/**
 * Shared policy for version-specific hooks: an absent fingerprint must disable
 * the optional feature rather than causing a patch-time failure.
 */
object PortingGuard {
    fun enabled(fingerprintMatched: Boolean, optional: Boolean = true): Boolean =
        fingerprintMatched || !optional
}
