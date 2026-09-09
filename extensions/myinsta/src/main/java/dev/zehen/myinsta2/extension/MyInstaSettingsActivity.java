package dev.zehen.myinsta2.extension;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/** MyInsta2 settings and diagnostics surface inside Instagram. */
public final class MyInstaSettingsActivity extends Activity {
    private static final int BG = Color.rgb(10, 10, 10);
    private static final int SURFACE = Color.rgb(22, 22, 22);
    private static final int TEXT = Color.rgb(245, 245, 245);
    private static final int MUTED = Color.rgb(160, 160, 160);
    private static final String PREFS = "myinsta2_settings";

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().getDecorView().setSystemUiVisibility(0);
        MyInstaDiagnostics.recordSettingsLaunch(this);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(18);
        root.setPadding(pad, dp(16), pad, dp(28));
        scroll.addView(root, new ScrollView.LayoutParams(-1, -2));

        TextView title = text("MyInsta Settings", 26, TEXT, true);
        root.addView(title, lp(0, 0, 1));
        TextView subtitle = text("MyInsta2", 13, MUTED, false);
        subtitle.setPadding(0, dp(3), 0, dp(16));
        root.addView(subtitle, lp(-1, -2, 0));

        section(root, "Ghost Mode");
        toggle(root, "Hide DM seen status", "Ghost Mode — DM seen", "ghost_dm_seen", true);
        toggle(root, "Hide typing status", "Ghost Mode — typing", "ghost_typing", true);
        toggle(root, "Hide story seen status", "Ghost Mode — stories", "ghost_stories", true);
        toggle(root, "Hide live heartbeat", "Ghost Mode — live", "ghost_live", true);
        toggle(root, "Prevent revoke notifications", "Anti-Revoke", "anti_revoke", true);

        section(root, "Distraction Free");
        toggle(root, "Hide ads", "Hide Ads", "hide_ads", true);
        toggle(root, "Hide suggested content", "Suggested content", "hide_suggested", true);
        toggle(root, "Disable video autoplay", "Video autoplay", "disable_video_autoplay", true);
        toggle(root, "Disable story auto-flip", "Story auto-flip", "disable_story_autoflip", true);

        section(root, "Downloads");
        info(root, "Download Media", "Instagram 445 downloader integration is validation-gated.");
        info(root, "Direct Thread Media", "DM media downloader anchor is ported; extraction remains validation-gated.");

        section(root, "Miscellaneous");
        info(root, "Copy Comment", "Copy-comment hook is ported to the 445 target.");
        info(root, "Copy Text", "Copy-text helpers are included in the runtime extension.");
        info(root, "Clickable Links", "Link interception helper is included in the runtime extension.");

        section(root, "Diagnostics");
        info(root, "Runtime status", "Diagnostics are generated from the running MyInsta2 process.");
        Button view = button("View Diagnostics");
        view.setOnClickListener(v -> showDiagnostics());
        root.addView(view, lp(-1, dp(46), 0));
        spacer(root, 6);
        Button copy = button("Copy Diagnostics");
        copy.setOnClickListener(v -> {
            try {
                MyInstaDiagnostics.copy(this);
                Toast.makeText(this, "Diagnostics copied", Toast.LENGTH_SHORT).show();
            } catch (Throwable error) {
                MyInstaDiagnostics.error(this, "Diagnostics", "copy", error);
                Toast.makeText(this, "Could not copy diagnostics", Toast.LENGTH_SHORT).show();
            }
        });
        root.addView(copy, lp(-1, dp(46), 0));
        spacer(root, 6);
        Button export = button("Export Bug Report");
        export.setOnClickListener(v -> exportBugReport());
        root.addView(export, lp(-1, dp(46), 0));
        spacer(root, 6);
        Button clear = button("Clear Recent Errors");
        clear.setOnClickListener(v -> {
            MyInstaDiagnostics.clearErrors(this);
            Toast.makeText(this, "Recent errors cleared", Toast.LENGTH_SHORT).show();
        });
        root.addView(clear, lp(-1, dp(46), 0));

        section(root, "About");
        info(root, "Developer", "Zehen");
        link(root, "Telegram", "@Zehen0i", "https://t.me/Zehen0i");
        link(root, "Support Chat", "@InstaEclipsechat", "https://t.me/InstaEclipsechat");
        info(root, "Credits", "Original MyInsta contributors and MyInsta2 development by Zehen.");

        setContentView(scroll);
    }

    private void showDiagnostics() {
        TextView report = text(MyInstaDiagnostics.buildReport(this), 12, TEXT, false);
        report.setTextIsSelectable(true);
        report.setPadding(dp(18), dp(12), dp(18), dp(12));
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(BG);
        scroll.addView(report, new ScrollView.LayoutParams(-1, -2));
        new android.app.AlertDialog.Builder(this)
                .setTitle("MyInsta2 Diagnostics")
                .setView(scroll)
                .setPositiveButton("Copy", (d, w) -> {
                    MyInstaDiagnostics.copy(this);
                    Toast.makeText(this, "Diagnostics copied", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Close", null)
                .show();
    }

    private void exportBugReport() {
        String report = MyInstaDiagnostics.buildReport(this);
        String name = "myinsta2-bug-report-" + System.currentTimeMillis() + ".txt";
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, name);
                values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/MyInsta2");
                Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri == null) throw new IllegalStateException("Downloads provider returned null");
                try (OutputStream out = getContentResolver().openOutputStream(uri)) {
                    if (out == null) throw new IllegalStateException("Could not open exported report");
                    out.write(report.getBytes(StandardCharsets.UTF_8));
                }
                Toast.makeText(this, "Bug report exported to Downloads/MyInsta2", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, name);
            startActivityForResult(intent, 4451);
        } catch (Throwable error) {
            MyInstaDiagnostics.error(this, "Diagnostics", "export", error);
            Toast.makeText(this, "Could not export bug report", Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 4451 || resultCode != RESULT_OK || data == null || data.getData() == null) return;
        try (OutputStream out = getContentResolver().openOutputStream(data.getData())) {
            if (out == null) throw new IllegalStateException("Could not open selected destination");
            out.write(MyInstaDiagnostics.buildReport(this).getBytes(StandardCharsets.UTF_8));
            Toast.makeText(this, "Bug report exported", Toast.LENGTH_SHORT).show();
        } catch (Throwable error) {
            MyInstaDiagnostics.error(this, "Diagnostics", "export-selected-file", error);
            Toast.makeText(this, "Could not write bug report", Toast.LENGTH_LONG).show();
        }
    }

    private void section(LinearLayout root, String title) {
        TextView v = text(title, 13, MUTED, true);
        v.setPadding(dp(4), dp(18), dp(4), dp(7));
        root.addView(v, lp(-1, -2, 0));
    }

    private void toggle(LinearLayout root, String title, String summary, String key, boolean defaultValue) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(14), dp(11), dp(8), dp(11));
        row.setBackgroundColor(SURFACE);
        LinearLayout labels = new LinearLayout(this);
        labels.setOrientation(LinearLayout.VERTICAL);
        labels.addView(text(title, 16, TEXT, false));
        labels.addView(text(summary, 12, MUTED, false));
        row.addView(labels, new LinearLayout.LayoutParams(0, -2, 1f));
        Switch sw = new Switch(this);
        sw.setChecked(getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean(key, defaultValue));
        sw.setOnCheckedChangeListener((button, value) -> getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit().putBoolean(key, value).apply());
        row.addView(sw, new LinearLayout.LayoutParams(-2, -2));
        root.addView(row, lp(-1, -2, 0));
        spacer(root, 1);
    }

    private void info(LinearLayout root, String title, String summary) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(dp(14), dp(11), dp(14), dp(11));
        row.setBackgroundColor(SURFACE);
        row.addView(text(title, 16, TEXT, false));
        row.addView(text(summary, 12, MUTED, false));
        root.addView(row, lp(-1, -2, 0));
        spacer(root, 1);
    }

    private void link(LinearLayout root, String title, String value, String url) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(dp(14), dp(11), dp(14), dp(11));
        row.setBackgroundColor(SURFACE);
        row.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))));
        row.addView(text(title, 16, TEXT, false));
        row.addView(text(value, 12, MUTED, false));
        root.addView(row, lp(-1, -2, 0));
        spacer(root, 1);
    }

    private Button button(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        return button;
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView v = new TextView(this);
        v.setText(value);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setTypeface(Typeface.DEFAULT, bold ? Typeface.BOLD : Typeface.NORMAL);
        return v;
    }

    private void spacer(LinearLayout root, int heightDp) {
        View v = new View(this);
        root.addView(v, lp(-1, dp(heightDp), 0));
    }

    private LinearLayout.LayoutParams lp(int w, int h, float weight) {
        return new LinearLayout.LayoutParams(w, h, weight);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
