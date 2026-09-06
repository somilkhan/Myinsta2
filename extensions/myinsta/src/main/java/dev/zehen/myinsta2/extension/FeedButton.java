package dev.zehen.myinsta2.extension;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.instagram.feed.media.mediaoption.MediaOption$Option;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Runtime side of the MyInsta2 feed downloader. */
public final class FeedButton {
    private static final String OPTION_TAG = "MYINSTA_DOWNLOAD";
    private static final Pattern URL_PATTERN = Pattern.compile("https?://[^\\s\\\"'<>]+", Pattern.CASE_INSENSITIVE);

    private FeedButton() {}

    public static MediaOption$Option downloadOverflowButton() {
        return new MediaOption$Option(OPTION_TAG, 500, 0);
    }

    public static MediaOption$Option[] addToMenuOptionArray() {
        MediaOption$Option[] original = MediaOption$Option.$values();
        MediaOption$Option[] result = new MediaOption$Option[original.length + 1];
        System.arraycopy(original, 0, result, 0, original.length);
        result[original.length] = downloadOverflowButton();
        return result;
    }

    public static void addFeedOverflowButton(Object buttonAdderObject, ArrayList<?> buttonList) {
        try {
            Class<?> creator = buttonAdderObject.getClass();
            Class<?> buttonType = Class.forName("LX.5xy");
            Method normal = buttonType.getDeclaredMethod("valueOf", String.class);
            Object normalButton = normal.invoke(null, "NORMAL");
            Method add = creator.getDeclaredMethod(
                    "A00", buttonType, MediaOption$Option.class, creator,
                    CharSequence.class, ArrayList.class, boolean.class);
            add.setAccessible(true);
            add.invoke(null, normalButton, downloadOverflowButton(), buttonAdderObject,
                    "Download", buttonList, false);
        } catch (Throwable ignored) {
            // Menu construction must never crash Instagram.
        }
    }

    /** Called only after the patch has resolved Instagram's actual feed media object. */
    public static boolean customButtonOnClick(MediaOption$Option option, Context context, Object mediaObject) {
        if (!isCustomButtonPressed(option)) return false;
        try {
            String url = findMediaUrl(mediaObject);
            if (url != null) enqueue(context, url);
        } catch (Throwable ignored) {
            // Downloader failure must never break the overflow handler.
        }
        return true;
    }

    public static boolean isCustomButtonPressed(MediaOption$Option option) {
        return option != null && OPTION_TAG.equals(readName(option));
    }

    private static String readName(MediaOption$Option option) {
        try {
            for (Field field : option.getClass().getDeclaredFields()) {
                if (field.getType() == String.class && !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object value = field.get(option);
                    if (value instanceof String) return (String) value;
                }
            }
        } catch (Throwable ignored) {}
        return String.valueOf(option).contains(OPTION_TAG) ? OPTION_TAG : "";
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

        Class<?> cls = value.getClass();
        for (Field field : cls.getDeclaredFields()) {
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

    private static void enqueue(Context context, String url) {
        if (context == null) return;
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager == null) return;
        String name = fileName(url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(name);
        request.setDescription("MyInsta2 download");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverMetered(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "MyInsta2/" + name);
        manager.enqueue(request);
    }

    private static String fileName(String url) {
        try {
            String path = Uri.parse(url).getPath();
            if (path != null) {
                int slash = path.lastIndexOf('/');
                if (slash >= 0 && slash + 1 < path.length()) {
                    String candidate = path.substring(slash + 1);
                    if (candidate.length() > 3 && candidate.length() < 120) return sanitize(candidate);
                }
            }
        } catch (Throwable ignored) {}
        return "myinsta2_" + System.currentTimeMillis() + ".mp4";
    }

    private static String sanitize(String name) {
        return name.replaceAll("[\\\\/:*?\\\"<>|]", "_");
    }
}
