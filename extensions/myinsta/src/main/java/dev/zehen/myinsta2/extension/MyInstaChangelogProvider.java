package dev.zehen.myinsta2.extension;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MyInstaChangelogProvider extends android.content.ContentProvider {
    private static final String PREFS = "myinsta2_changelog";
    private static final String LAST_VERSION = "last_version";
    private static final String TELEGRAM = "https://t.me/Zehen0i";
    private static final String SUPPORT = "https://t.me/InstaEclipsechat";

    private static final String[] CHANGES = new String[]{
            "First-launch and post-update changelog popup.",
            "MyInsta2 settings surface foundation.",
            "Instagram 445 stability and runtime helper fixes."
    };

    private boolean shownThisProcess;

    private final Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
        @Override public void onActivityResumed(Activity activity) {
            if (shownThisProcess || activity.isFinishing()) return;
            if (!shouldShow(activity)) return;
            shownThisProcess = true;
            activity.runOnUiThread(() -> showDialog(activity));
        }
        @Override public void onActivityCreated(Activity a, Bundle b) {}
        @Override public void onActivityStarted(Activity a) {}
        @Override public void onActivityPaused(Activity a) {}
        @Override public void onActivityStopped(Activity a) {}
        @Override public void onActivitySaveInstanceState(Activity a, Bundle b) {}
        @Override public void onActivityDestroyed(Activity a) {}
    };

    @Override public boolean onCreate() {
        Context context = getContext();
        if (context == null) return false;
        Context appContext = context.getApplicationContext();
        if (!(appContext instanceof Application)) return false;
        ((Application) appContext).registerActivityLifecycleCallbacks(callbacks);
        return true;
    }

    private boolean shouldShow(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return !getVersionKey(activity).equals(prefs.getString(LAST_VERSION, ""));
    }

    private String getVersionKey(Context context) {
        try {
            android.content.pm.PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            long versionCode = android.os.Build.VERSION.SDK_INT >= 28
                    ? info.getLongVersionCode() : info.versionCode;
            return versionCode + "|" + String.valueOf(info.versionName);
        } catch (Throwable ignored) {
            return "unknown";
        }
    }

    private String getVersionName(Context context) {
        try {
            android.content.pm.PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info.versionName == null ? "Unknown" : info.versionName;
        } catch (Throwable ignored) {
            return "Unknown";
        }
    }

    private void showDialog(Activity activity) {
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(activity, 24);
        root.setPadding(pad, dp(activity, 4), pad, 0);

        addSection(root, "Version", getVersionName(activity), false, null);

        StringBuilder changes = new StringBuilder();
        for (String change : CHANGES) changes.append("• ").append(change).append('\n');
        addSection(root, "Changes", changes.toString().trim(), false, null);

        addSection(root, "Developer", "Zehen", false, null);
        addSection(root, "Telegram", "@Zehen0i", true, TELEGRAM);
        addSection(root, "Support Chat", "@InstaEclipsechat", true, SUPPORT);
        addSection(root, "Credits", "Original MyInsta contributors and open-source contributors.", false, null);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("MyInsta2")
                .setView(root)
                .setPositiveButton("Got it", (d, which) -> markSeen(activity))
                .setOnCancelListener(d -> markSeen(activity))
                .create();
        dialog.show();
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }
    }

    private void addSection(LinearLayout root, String label, String value, boolean link, String url) {
        Context context = root.getContext();
        TextView labelView = new TextView(context);
        labelView.setText(label);
        labelView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        labelView.setTextSize(13);
        root.addView(labelView, new LinearLayout.LayoutParams(-1, -2));

        TextView valueView = new TextView(context);
        valueView.setTextSize(15);
        valueView.setPadding(0, 0, 0, dp(context, 12));
        if (link) {
            SpannableString span = new SpannableString(value);
            span.setSpan(new ClickableSpan() {
                @Override public void onClick(View widget) { openTelegram(context, url); }
                @Override public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(true);
                    ds.setTypeface(Typeface.DEFAULT);
                }
            }, 0, value.length(), 0);
            valueView.setText(span);
            valueView.setMovementMethod(LinkMovementMethod.getInstance());
            valueView.setLinksClickable(true);
        } else {
            valueView.setText(value);
        }
        root.addView(valueView, new LinearLayout.LayoutParams(-1, -2));
    }

    private void openTelegram(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            intent.setPackage("org.telegram.messenger");
            context.startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
            intent.setPackage(null);
            context.startActivity(intent);
        }
    }

    private void markSeen(Context context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putString(LAST_VERSION, getVersionKey(context)).apply();
    }

    private static int dp(Context context, int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }

    @Override public String getType(Uri uri) { return null; }
    @Override public Uri insert(Uri uri, android.content.ContentValues values) { return null; }
    @Override public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }
    @Override public int update(Uri uri, android.content.ContentValues values, String selection, String[] selectionArgs) { return 0; }
    @Override public android.database.Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) { return null; }
}
