package dev.zehen.myinsta2.download

/**
 * Patch-module side of the downloader. Android framework classes must not be
 * referenced here because the Morphe patch artifact is compiled against the
 * patching API. Runtime Android code is injected into the target APK instead.
 */
internal object DownloadRuntime {
    @JvmStatic
    fun isSupportedMediaUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        return url.startsWith("https://", ignoreCase = true) ||
            url.startsWith("http://", ignoreCase = true)
    }
}
