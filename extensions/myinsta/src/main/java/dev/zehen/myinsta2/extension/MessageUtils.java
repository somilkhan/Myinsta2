package dev.zehen.myinsta2.extension;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Runtime helper for the guarded Instagram 445 DM media hook. */
public final class MessageUtils {
    private static final Pattern URL_PATTERN = Pattern.compile("https?://[^\\s\\\"'<>]+", Pattern.CASE_INSENSITIVE);

    private MessageUtils() {}

    /**
     * Returns true only when a usable media URL was found and queued. Returning
     * false deliberately lets Instagram's original saver continue unchanged.
     */
    public static boolean messageDownloadCheck(Context context, Object message) {
        try {
            String url = findMediaUrl(message);
            if (url == null || context == null) return false;

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager == null) return false;

            String name = fileName(url);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(name);
            request.setDescription("MyInsta2 DM download");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setAllowedOverMetered(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "MyInsta2/" + name);
            manager.enqueue(request);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static String findMediaUrl(Object root) {
        if (root == null) return null;
        Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
        return findUrl(root, 0, visited);
    }

    private static String findUrl(Object value, int depth, Set<Object> visited) {
        if (value == null || depth > 5) return null;
        if (value instanceof String) return chooseUrl((String) value);
        if (value.getClass().isPrimitive()) return null;
        if (!visited.add(value)) return null;

        if (value instanceof Iterable<?>) {
            for (Object item : (Iterable<?>) value) {
                String url = findUrl(item, depth + 1, visited);
                if (url != null) return url;
            }
        }

        for (Field field : value.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            try {
                field.setAccessible(true);
                String url = findUrl(field.get(value), depth + 1, visited);
                if (url != null) return url;
            } catch (Throwable ignored) {}
        }
        return chooseUrl(value.toString());
    }

    private static String chooseUrl(String text) {
        if (text == null || text.length() > 20000) return null;
        Matcher matcher = URL_PATTERN.matcher(text);
        String fallback = null;
        while (matcher.find()) {
            String url = matcher.group();
            while (url.endsWith(")") || url.endsWith("]") || url.endsWith("}") || url.endsWith(",")) {
                url = url.substring(0, url.length() - 1);
            }
            String lower = url.toLowerCase();
            if (lower.contains("cdninstagram") || lower.contains("fbcdn")) return url;
            if (fallback == null && (lower.contains("instagram") || lower.contains(".mp4") || lower.contains(".jpg") || lower.contains(".jpeg") || lower.contains(".png"))) {
                fallback = url;
            }
        }
        return fallback;
    }

    private static String fileName(String url) {
        try {
            String path = Uri.parse(url).getPath();
            if (path != null) {
                int slash = path.lastIndexOf('/');
                if (slash >= 0 && slash + 1 < path.length()) {
                    String candidate = path.substring(slash + 1);
                    if (candidate.length() > 3 && candidate.length() < 120) {
                        return sanitize(candidate);
                    }
                }
            }
        } catch (Throwable ignored) {}
        return "myinsta2_dm_" + System.currentTimeMillis() + ".mp4";
    }

    private static String sanitize(String name) {
        return name.replaceAll("[\\\\/:*?\\\"<>|]", "_");
    }
}
