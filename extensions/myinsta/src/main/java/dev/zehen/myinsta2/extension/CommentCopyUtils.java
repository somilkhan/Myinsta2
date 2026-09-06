package dev.zehen.myinsta2.extension;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/** Runtime handler for the exact Instagram 445 comment-copy action. */
@SuppressWarnings("unused")
public final class CommentCopyUtils {
    private CommentCopyUtils() {}

    public static void addButton(List list, Object commentObject) {
        try {
            if (list == null || commentObject == null) return;
            String text = readCommentText(commentObject);
            if (text != null && !text.trim().isEmpty()) {
                list.add(CopyTextButton.A00);
            }
        } catch (Throwable ignored) {
            // The original Instagram comment menu must remain untouched on failure.
        }
    }

    /** Return true only when the custom button consumed the click. */
    public static boolean checkOnCommentButtonClick(Object button, List list) {
        try {
            if (button != CopyTextButton.A00 || list == null || list.isEmpty()) return false;

            Object commentObject = list.get(0);
            String text = readCommentText(commentObject);
            if (text == null || text.trim().isEmpty()) return true;

            Context context = getApplicationContext();
            if (context == null) return true;

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                clipboard.setPrimaryClip(ClipData.newPlainText("Instagram comment", text));
                Toast.makeText(context, "Comment copied", Toast.LENGTH_SHORT).show();
            }
            return true;
        } catch (Throwable ignored) {
            return true;
        }
    }

    private static String readCommentText(Object commentObject) throws ReflectiveOperationException {
        if (commentObject == null) return null;

        // Exact 445 comment model/field discovered in LX/FZO.A19.
        Field field = commentObject.getClass().getDeclaredField("A0O");
        field.setAccessible(true);
        Object value = field.get(commentObject);
        return value instanceof String ? (String) value : null;
    }

    private static Context getApplicationContext() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentApplication = activityThread.getDeclaredMethod("currentApplication");
            currentApplication.setAccessible(true);
            Object application = currentApplication.invoke(null);
            return application instanceof Context ? ((Context) application).getApplicationContext() : null;
        } catch (Throwable ignored) {
            return null;
        }
    }
}
