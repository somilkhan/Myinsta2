package dev.zehen.myinsta2.extension;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

/** Exact Instagram-445 profile action-bar entry point for MyInsta2 settings. */
public final class MyInstaSettingsEntryPoint {
    private static final String ACTIVITY = "dev.zehen.myinsta2.extension.MyInstaSettingsActivity";
    private static final String ORIGINAL_RESOURCE_PACKAGE = "com.instagram.android";
    private static final String[] OVERFLOW_IDS = {
            "action_bar_overflow_icon", "action_bar_overflow", "action_bar_overflow_button",
            "overflow_button", "overflow_button_right", "overflow_button_layout",
            "more_button", "more_button_click_area", "overflow_menu"
    };
    private static final long[] ARM_DELAYS_MS = {50L, 150L, 400L, 800L, 1500L, 2500L, 4000L, 6000L};
    private static final long REARM_WINDOW_MS = 7000L;
    private static final long LAUNCH_DEBOUNCE_MS = 1200L;

    private MyInstaSettingsEntryPoint() {}

    /** Called from the verified Instagram 445 LX/Dyw.A05 -> LX/gAr.A04 path. */
    public static void onProfileActionBarReady(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        try {
            View root = activity.getWindow().getDecorView();
            if (!(root instanceof ViewGroup)) return;
            MyInstaDiagnostics.recordSettingsHook(activity);
            armUntilStable(activity, root, 0);
        } catch (Throwable error) {
            MyInstaDiagnostics.error(activity, "SettingsEntryPoint", "arm", error);
        }
    }

    private static void armUntilStable(Activity activity, View root, int attempt) {
        if (activity.isFinishing() || root == null || !root.isAttachedToWindow()) return;

        View overflow = findOverflowCandidate(root, activity);
        if (overflow != null) {
            armListenerChain(activity, overflow);
        }

        if (attempt >= ARM_DELAYS_MS.length) return;
        final View retryRoot = root;
        final long delay = ARM_DELAYS_MS[attempt];
        root.postDelayed(() -> armUntilStable(activity, retryRoot, attempt + 1), delay);
    }

    private static View findOverflowCandidate(View root, Activity activity) {
        for (String name : OVERFLOW_IDS) {
            int id = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
            if (id == 0) {
                id = activity.getResources().getIdentifier(name, "id", ORIGINAL_RESOURCE_PACKAGE);
            }
            if (id == 0) continue;
            View view = root.findViewById(id);
            if (isVisible(view)) return view;
        }
        return null;
    }

    private static void armListenerChain(Activity activity, View candidate) {
        long deadline = System.currentTimeMillis() + REARM_WINDOW_MS;
        View current = candidate;
        int depth = 0;
        while (current != null && depth++ < 4) {
            if (isVisible(current)) {
                installLongPress(current, activity, deadline);
            }
            current = current.getParent() instanceof View ? (View) current.getParent() : null;
        }
    }

    private static void installLongPress(View view, Activity activity, long deadline) {
        view.setOnLongClickListener(v -> {
            long now = System.currentTimeMillis();
            Long lastLaunch = (Long) v.getTag(android.R.id.custom);
            if (lastLaunch != null && now - lastLaunch < LAUNCH_DEBOUNCE_MS) return true;
            v.setTag(android.R.id.custom, now);

            try {
                Intent intent = new Intent(activity, Class.forName(ACTIVITY));
                activity.startActivity(intent);
                MyInstaDiagnostics.recordSettingsLaunch(activity);
                return true;
            } catch (Throwable error) {
                MyInstaDiagnostics.error(activity, "SettingsEntryPoint", "launch", error);
                return false;
            }
        });

        // Instagram/Piko may replace the view listener after the action-bar is built.
        // Keep re-arming the same resource-targeted view chain for a bounded window so
        // view recreation and late listener installation cannot silently remove the
        // MyInsta2 long-press gesture.
        if (System.currentTimeMillis() < deadline && view.isAttachedToWindow()) {
            view.postDelayed(() -> {
                if (view.isAttachedToWindow() && activity.getWindow() != null && !activity.isFinishing()) {
                    installLongPress(view, activity, deadline);
                }
            }, 250L);
        }
    }

    private static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE && view.isShown();
    }
}
