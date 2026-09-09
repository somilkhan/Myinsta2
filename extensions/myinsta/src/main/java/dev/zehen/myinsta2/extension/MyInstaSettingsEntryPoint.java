package dev.zehen.myinsta2.extension;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Installs the MyInsta settings gesture on Instagram's self-profile overflow
 * control. This code deliberately does not depend on view coordinates or
 * visible text.
 */
public final class MyInstaSettingsEntryPoint {
    private static final String ACTIVITY = "dev.zehen.myinsta2.extension.MyInstaSettingsActivity";
    private static final String ORIGINAL_RESOURCE_PACKAGE = "com.instagram.android";

    private static final String[] OVERFLOW_IDS = {
            "action_bar_overflow_icon",
            "action_bar_overflow",
            "action_bar_overflow_button",
            "overflow_menu"
    };
    private static final String[] SELF_PROFILE_IDS = {
            "self_profile_switcher",
            "profile_switcher"
    };

    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static boolean installed;
    private static final java.util.Set<ViewTreeObserver> watchedRoots =
            java.util.Collections.newSetFromMap(new java.util.WeakHashMap<>());

    private MyInstaSettingsEntryPoint() {}

    public static void install(Application application) {
        if (application == null || installed) return;
        installed = true;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override public void onActivityResumed(Activity activity) { arm(activity); }
            @Override public void onActivityCreated(Activity a, Bundle b) {}
            @Override public void onActivityStarted(Activity a) {}
            @Override public void onActivityPaused(Activity a) {}
            @Override public void onActivityStopped(Activity a) {}
            @Override public void onActivitySaveInstanceState(Activity a, Bundle b) {}
            @Override public void onActivityDestroyed(Activity a) {}
        });
    }

    private static void arm(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        MAIN.post(() -> {
            if (activity.isFinishing()) return;
            View root = activity.getWindow().getDecorView();
            if (!(root instanceof ViewGroup)) return;

            installOnProfile(activity, root);
            ViewTreeObserver observer = root.getViewTreeObserver();
            synchronized (watchedRoots) {
                if (observer.isAlive() && !watchedRoots.contains(observer)) {
                    watchedRoots.add(observer);
                    observer.addOnGlobalLayoutListener(() -> installOnProfile(activity, root));
                }
            }
            scheduleRetries(activity, root);
        });
    }

    private static void scheduleRetries(Activity activity, View root) {
        final int[] delays = {100, 300, 750, 1500, 3000};
        for (int delay : delays) {
            MAIN.postDelayed(() -> {
                if (!activity.isFinishing() && root.isAttachedToWindow()) {
                    installOnProfile(activity, root);
                }
            }, delay);
        }
    }

    private static void installOnProfile(Activity activity, View root) {
        if (activity.isFinishing() || !(root instanceof ViewGroup)) return;

        Context context = activity;
        View selfMarker = findByAnyResourceName(root, context, SELF_PROFILE_IDS);
        if (!isVisible(selfMarker)) return;

        View overflow = findByAnyResourceName(root, context, OVERFLOW_IDS);
        if (!isVisible(overflow)) return;

        // Do not cache the View object. Instagram can recycle/rebind the same
        // view and replace its listener after profile UI inflation.
        overflow.setOnLongClickListener(v -> {
            if (!isStillOwnProfile(root, context)) return false;
            try {
                Intent intent = new Intent(activity, Class.forName(ACTIVITY));
                activity.startActivity(intent);
                return true;
            } catch (Throwable ignored) {
                return false;
            }
        });
    }

    private static boolean isStillOwnProfile(View root, Context context) {
        View self = findByAnyResourceName(root, context, SELF_PROFILE_IDS);
        View overflow = findByAnyResourceName(root, context, OVERFLOW_IDS);
        return isVisible(self) && isVisible(overflow);
    }

    private static View findByAnyResourceName(View root, Context context, String[] names) {
        for (String name : names) {
            int id = context.getResources().getIdentifier(name, "id", context.getPackageName());
            if (id == 0) {
                id = context.getResources().getIdentifier(name, "id", ORIGINAL_RESOURCE_PACKAGE);
            }
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
