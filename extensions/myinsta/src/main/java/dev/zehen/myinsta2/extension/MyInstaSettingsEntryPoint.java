package dev.zehen.myinsta2.extension;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

/** Exact Instagram-445 profile action-bar entry point for MyInsta2 settings. */
public final class MyInstaSettingsEntryPoint {
    private static final String ACTIVITY = "dev.zehen.myinsta2.extension.MyInstaSettingsActivity";
    private static final String ORIGINAL_RESOURCE_PACKAGE = "com.instagram.android";
    private static final String[] ACTION_BAR_IDS = {"profile_action_bar", "profile_action_bar_stub"};
    private static final String[] SELF_PROFILE_IDS = {"self_profile_switcher", "profile_switcher"};
    private static final String[] OVERFLOW_IDS = {
            "action_bar_overflow_icon", "action_bar_overflow", "action_bar_overflow_button",
            "overflow_button", "overflow_button_right", "overflow_button_layout",
            "more_button", "more_button_click_area", "overflow_menu"
    };

    private MyInstaSettingsEntryPoint() {}

    /** Called from the verified Instagram 445 LX/Dyw.A05 -> LX/gAr.A04 path. */
    public static void onProfileActionBarReady(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        try {
            View root = activity.getWindow().getDecorView();
            if (!(root instanceof ViewGroup)) return;
            MyInstaDiagnostics.recordSettingsHook(activity);
            arm(activity, root, 0);
        } catch (Throwable error) {
            MyInstaDiagnostics.error(activity, "SettingsEntryPoint", "arm", error);
        }
    }

    private static void arm(Activity activity, View root, int attempt) {
        View resolvedScope = findByAnyResourceName(root, activity, ACTION_BAR_IDS);
        if (resolvedScope == null) resolvedScope = root;
        final View scope = resolvedScope;

        View selfMarker = findByAnyResourceName(scope, activity, SELF_PROFILE_IDS);
        View overflow = findByAnyResourceName(scope, activity, OVERFLOW_IDS);
        if (isVisible(selfMarker) && isVisible(overflow)) {
            final View finalOverflow = overflow;
            finalOverflow.setOnLongClickListener(v -> {
                try {
                    if (!isOwnProfile(root, activity)) return false;
                    Intent intent = new Intent(activity, Class.forName(ACTIVITY));
                    activity.startActivity(intent);
                    MyInstaDiagnostics.recordSettingsLaunch(activity);
                    return true;
                } catch (Throwable error) {
                    MyInstaDiagnostics.error(activity, "SettingsEntryPoint", "launch", error);
                    return false;
                }
            });
            return;
        }

        if (attempt >= 5) return;
        long delay = new long[]{50, 150, 400, 800, 1500}[attempt];
        final View retryRoot = root;
        root.postDelayed(() -> {
            if (!activity.isFinishing() && retryRoot.isAttachedToWindow()) {
                arm(activity, retryRoot, attempt + 1);
            }
        }, delay);
    }

    private static boolean isOwnProfile(View root, Context context) {
        View scope = findByAnyResourceName(root, context, ACTION_BAR_IDS);
        if (scope == null) scope = root;
        return isVisible(findByAnyResourceName(scope, context, SELF_PROFILE_IDS));
    }

    private static View findByAnyResourceName(View root, Context context, String[] names) {
        if (root == null) return null;
        for (String name : names) {
            int id = context.getResources().getIdentifier(name, "id", context.getPackageName());
            if (id == 0) id = context.getResources().getIdentifier(name, "id", ORIGINAL_RESOURCE_PACKAGE);
            if (id != 0) {
                View view = root.findViewById(id);
                if (view != null) return view;
            }
        }
        return null;
    }

    private static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE && view.isShown();
    }
}
