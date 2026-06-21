package com.armada.expiryapp.util;

import android.content.Context;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.WorkManager;
import com.armada.expiryapp.worker.ReminderWorker;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/armada/expiryapp/util/ReminderScheduler;", "", "<init>", "()V", "WORK_NAME", "", "scheduleIfNeeded", "", "context", "Landroid/content/Context;", "reschedule", "buildRequest", "Landroidx/work/PeriodicWorkRequest;", "msToNextReminder", "", "app_debug"})
public final class ReminderScheduler {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String WORK_NAME = "armada_monthly_reminder";
    @org.jetbrains.annotations.NotNull()
    public static final com.armada.expiryapp.util.ReminderScheduler INSTANCE = null;
    
    private ReminderScheduler() {
        super();
    }
    
    public final void scheduleIfNeeded(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void reschedule(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    private final androidx.work.PeriodicWorkRequest buildRequest() {
        return null;
    }
    
    private final long msToNextReminder() {
        return 0L;
    }
}