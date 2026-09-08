package dev.zehen.myinsta2.extension;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Installs the MyInsta settings gesture on Instagram's real profile overflow
 * control. No screen coordinates or fixed view hierarchy positions are used.
 */
public final class MyInstaSettingsEntryPoint {
    private static final String ACTIVITY = "dev.zehen.myinsta2.extension.MyInstaSettingsActivity";
    private static final String OVERFLOW_ID = "action_bar_overflow_icon";
    private static final String SELF_PROFILE_MARKER_ID = "self_profile_switcher";
    private static boolean installed;
    private static final java.util.Set<View> hooked =
            java.util.Collections.newSetFromMap(new java.util.WeakHashMap<>());

    private MyInstaSettingsEntryPoint() {}

    public static void install(Application application) {
        if (application == null || installed) return;
        installed = true;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override public void onActivityResumed(Activity activity) {
                installOnProfile(activity);
            }
            @Override public void onActivityCreated(Activity a, Bundle b) {}
            @Override public void onActivityStarted(Activity a) {}
            @Override public void onActivityPaused(Activity a) {}
            @Override public void onActivityStopped(Activity a) {}
            @Override public void onActivitySaveInstanceState(Activity a, Bundle b) {}
            @Override public void onActivityDestroyed(Activity a) {}
        });
    }

    private static void installOnProfile(Activity activity) {
        if (activity.isFinishing()) return;
        View root = activity.getWindow().getDecorView();
        if (!(root instanceof ViewGroup)) return;

        Context context = activity;
        int selfId = context.getResources().getIdentifier(
                SELF_PROFILE_MARKER_ID, "id", context.getPackageName());
        int overflowId = context.getResources().getIdentifier(
                OVERFLOW_ID, "id", context.getPackageName());
        if (selfId == 0 || overflowId == 0) return;

        View selfMarker = root.findViewById(selfId);
        if (selfMarker == null || selfMarker.getVisibility() != View.VISIBLE) return;

        View overflow = root.findViewById(overflowId);
        if (overflow == null || overflow.getVisibility() != View.VISIBLE || !overflow.isShown()) return;
        synchronized (hooked) {
            if (hooked.contains(overflow)) return;
            hooked.add(overflow);
        }
        overflow.setOnLongClickListener(v -> {
            if (!isStillOwnProfile(root, selfId, overflowId)) return false;
            try {
                Intent intent = new Intent(activity, Class.forName(ACTIVITY));
                activity.startActivity(intent);
                return true;
            } catch (Throwable ignored) {
                return false;
            }
        });
    }

    private static boolean isStillOwnProfile(View root, int selfId, int overflowId) {
        View self = root.findViewById(selfId);
        View overflow = root.findViewById(overflowId);
        return self != null && self.getVisibility() == View.VISIBLE
                && overflow != null && overflow.getVisibility() == View.VISIBLE && overflow.isShown();
    }
}
