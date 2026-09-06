package dev.zehen.myinsta2.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.lang.reflect.Modifier
import java.util.Collections
import java.util.IdentityHashMap

/** Runtime side of the 445 feed downloader. It deliberately uses reflection so
 * the extension does not depend on Instagram's obfuscated model classes. */
object DownloadRuntime {
    private const val OPTION_TAG = "MYINSTA_DOWNLOAD"
    private var option: Any? = null

    @JvmStatic
    fun addDownloadButton(buttonAdder: Any, buttonList: ArrayList<*>) {
        try {
            val option = downloadOption() ?: return
            val methods = buttonAdder.javaClass.declaredMethods
            val add = methods.firstOrNull { m ->
                Modifier.isStatic(m.modifiers) &&
                    m.name == "A00" &&
                    m.parameterTypes.size == 6 &&
                    m.parameterTypes[1].name.contains("MediaOption")
            } ?: return
            add.isAccessible = true

            val normal = normalButton(add.parameterTypes[0]) ?: return
            add.invoke(null, normal, option, buttonAdder, "Download", buttonList, false)
        } catch (_: Throwable) {
            // Never break Instagram's menu because the optional button failed.
        }
    }

    @JvmStatic
    fun isDownloadOption(value: Any?): Boolean = value != null && value === option

    @JvmStatic
    fun downloadCurrentMedia(controller: Any) {
        try {
            val media = controller.javaClass.getDeclaredMethod("A00", controller.javaClass).let {
                it.isAccessible = true
                it.invoke(null, controller)
            } ?: return
            val url = findMediaUrl(media) ?: return
            val context = currentApplication() ?: return
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("Instagram media")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    "Instagram/" + fileName(url),
                )
            val mime = if (isVideo(url)) "video/mp4" else "image/jpeg"
            request.setMimeType(mime)
            context.getSystemService(Context.DOWNLOAD_SERVICE)?.let {
                (it as DownloadManager).enqueue(request)
                Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
            }
        } catch (_: Throwable) {
            currentApplication()?.let { Toast.makeText(it, "Download failed", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun downloadOption(): Any? {
        option?.let { return it }
        return try {
            val c = Class.forName("com.instagram.feed.media.mediaoption.MediaOption$Option")
            val ctor = c.declaredConstructors.firstOrNull { it.parameterTypes.size == 3 } ?: return null
            ctor.isAccessible = true
            ctor.newInstance(OPTION_TAG, 599, 0).also { option = it }
        } catch (_: Throwable) { null }
    }

    private fun normalButton(type: Class<*>): Any? = try {
        if (type.isEnum) java.lang.Enum.valueOf(type.asSubclass(Enum::class.java), "NORMAL")
        else type.getDeclaredMethod("valueOf", String::class.java).let {
            it.isAccessible = true
            it.invoke(null, "NORMAL")
        }
    } catch (_: Throwable) { null }

    private fun currentApplication(): Context? = try {
        Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication").invoke(null) as? Context
    } catch (_: Throwable) { null }

    private fun findMediaUrl(root: Any): String? {
        val seen = Collections.newSetFromMap(IdentityHashMap<Any, Boolean>())
        return find(root, 0, seen)
    }

    private fun find(value: Any?, depth: Int, seen: MutableSet<Any>): String? {
        if (value == null || depth > 5 || seen.size > 500 || !seen.add(value)) return null
        if (value is String) return value.takeIf { it.startsWith("https://") && (it.contains("cdninstagram.com") || it.contains("fbcdn.net")) }
        if (value is Iterable<*>) for (item in value) find(item, depth + 1, seen)?.let { return it }
        if (value.javaClass.isArray) {
            for (i in 0 until java.lang.reflect.Array.getLength(value)) find(java.lang.reflect.Array.get(value, i), depth + 1, seen)?.let { return it }
        }
        for (field in value.javaClass.declaredFields) {
            try {
                field.isAccessible = true
                val child = field.get(value)
                if (child is String && field.name.contains("url", true)) {
                    find(child, depth + 1, seen)?.let { return it }
                }
                if (!field.type.isPrimitive && field.type != String::class.java) {
                    find(child, depth + 1, seen)?.let { return it }
                }
            } catch (_: Throwable) { }
        }
        return null
    }

    private fun isVideo(url: String) = url.contains("video", true) || url.contains(".mp4", true)

    private fun fileName(url: String): String {
        val base = url.substringBefore('?').substringAfterLast('/').ifBlank { "media" }
        val clean = base.replace(Regex("[^A-Za-z0-9._-]"), "_")
        return if (clean.contains('.')) clean else clean + if (isVideo(url)) ".mp4" else ".jpg"
    }
}
