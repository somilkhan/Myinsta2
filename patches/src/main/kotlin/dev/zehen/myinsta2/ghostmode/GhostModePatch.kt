package dev.zehen.myinsta2.ghostmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.zehen.myinsta2.shared.Constants.INSTAGRAM_445

private object MarkThreadSeenFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf(
        "Lcom/instagram/common/session/UserSession;",
        "LX/1ew;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
    ),
    strings = listOf("mark_thread_seen-"),
)

@Suppress("unused")
val ghostModePatch = bytecodePatch(
    name = "Ghost Mode",
    description = "Stops Instagram from sending direct-message seen events when Ghost Mode is enabled.",
    default = true,
) {
    compatibleWith(INSTAGRAM_445)

    execute {
        MarkThreadSeenFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Landroid/app/ActivityThread;->currentApplication()Landroid/app/Application;
                move-result-object v0
                invoke-static {v0}, Landroid/preference/PreferenceManager;->getDefaultSharedPreferences(Landroid/content/Context;)Landroid/content/SharedPreferences;
                move-result-object v0
                const-string v1, \"remove_seen_dms\"
                const/4 v2, 0x0
                invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z
                move-result v0
                if-eqz v0, :ghost_mode_continue
                return-void
                :ghost_mode_continue
            """.trimIndent(),
        )
    }
}
