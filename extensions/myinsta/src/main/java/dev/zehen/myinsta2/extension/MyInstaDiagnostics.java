package dev.zehen.myinsta2.extension;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/** Runtime diagnostics and bounded MyInsta2 error buffer. */
public final class MyInstaDiagnostics {
    private static final String TAG = "MyInsta2";
    private static final String PREFS = "myinsta2_diagnostics";
    private static final String ERRORS = "recent_errors";
    private static final String HOOK_COUNT = "settings_hook_count";
    private static final String LAUNCH_COUNT = "settings_launch_count";
    private static final int MAX_ERRORS = 20;
    private static final int MAX_ERROR_CHARS = 3500;

    private MyInstaDiagnostics() {}

    public static void recordSettingsHook(Context context) { increment(context, HOOK_COUNT); }
    public static void recordSettingsLaunch(Context context) { increment(context, LAUNCH_COUNT); }

    public static void error(Context context, String component, String stage, Throwable throwable) {
        if (context == null) return;
        try {
            StringWriter sw = new StringWriter();
            if (throwable != null) throwable.printStackTrace(new PrintWriter(sw));
            String stack = sw.toString();
            String message = String.format(Locale.US, "[%s] %s\n%s", component, stage,
                    stack.isEmpty() ? "Unknown error" : stack);
            if (message.length() > MAX_ERROR_CHARS) message = message.substring(0, MAX_ERROR_CHARS) + "\n[truncated]";
            SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            String old = prefs.getString(ERRORS, "");
            String value = message + (old.isEmpty() ? "" : "\n\n" + old);
            String[] entries = value.split("\\n\\n");
            StringBuilder bounded = new StringBuilder();
            for (int i = 0; i < entries.length && i < MAX_ERRORS; i++) {
                if (i > 0) bounded.append("\n\n");
                bounded.append(entries[i]);
            }
            prefs.edit().putString(ERRORS, bounded.toString()).apply();
            Log.e(TAG, component + " / " + stage, throwable);
        } catch (Throwable ignored) {
            // Diagnostics must never become a source of application crashes.
        }
    }

    public static String buildReport(Context context) {
        StringBuilder out = new StringBuilder(8192);
        append(out, "MyInsta2", "Diagnostics");
        append(out, "MyInsta2 version", packageVersion(context));
        append(out, "Instagram version", packageVersion(context));
        append(out, "Android", Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")");
        append(out, "Device", Build.MANUFACTURER + " " + Build.MODEL);
        append(out, "Architecture", Build.SUPPORTED_ABIS != null && Build.SUPPORTED_ABIS.length > 0 ? Build.SUPPORTED_ABIS[0] : "Unknown");
        append(out, "Target", "Instagram 445.0.0.45.83 / arm64-v8a");
        append(out, "Runtime extension", "Loaded — diagnostics provider is executing");

        SharedPreferences settings = context.getSharedPreferences("myinsta2_settings", Context.MODE_PRIVATE);
        out.append("\nEnabled feature settings:\n");
        String[] keys = {"ghost_dm_seen", "ghost_typing", "ghost_stories", "ghost_live", "anti_revoke", "hide_ads", "hide_suggested", "disable_video_autoplay", "disable_story_autoflip"};
        for (String key : keys) out.append("- ").append(key).append(" = ").append(settings.getBoolean(key, true)).append('\n');

        SharedPreferences diagnostics = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        append(out, "Settings hook invocations", String.valueOf(diagnostics.getInt(HOOK_COUNT, 0)));
        append(out, "Settings launches", String.valueOf(diagnostics.getInt(LAUNCH_COUNT, 0)));
        append(out, "Timestamp", String.valueOf(System.currentTimeMillis()));
        out.append("\nRecent MyInsta2 errors:\n");
        String errors = diagnostics.getString(ERRORS, "");
        out.append(errors.isEmpty() ? "None" : errors);
        return out.toString();
    }

    public static void copy(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) return;
        clipboard.setPrimaryClip(ClipData.newPlainText("MyInsta2 diagnostics", buildReport(context)));
    }

    public static void clearErrors(Context context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().remove(ERRORS).apply();
    }

    private static void increment(Context context, String key) {
        SharedPreferences p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        p.edit().putInt(key, p.getInt(key, 0) + 1).apply();
    }

    private static String packageVersion(Context context) {
        try {
            android.content.pm.PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            long code = Build.VERSION.SDK_INT >= 28 ? info.getLongVersionCode() : info.versionCode;
            return String.valueOf(info.versionName) + " (" + code + ")";
        } catch (Throwable ignored) {
            return "Unknown";
        }
    }

    private static void append(StringBuilder out, String key, String value) {
        out.append(key).append(": ").append(value).append('\n');
    }
}
