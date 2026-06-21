package com.armada.expiryapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.StrictMode;
import com.armada.expiryapp.util.ArchiveScheduler;
import com.armada.expiryapp.util.ArmadaCrashHandler;
import com.armada.expiryapp.util.ReminderScheduler;
import com.armada.expiryapp.worker.ReminderWorker;
import dagger.hilt.android.HiltAndroidApp;

@dagger.hilt.android.HiltAndroidApp()
@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0005H\u0002J\b\u0010\u0007\u001a\u00020\u0005H\u0002\u00a8\u0006\b"}, d2 = {"Lcom/armada/expiryapp/ArmadaApplication;", "Landroid/app/Application;", "<init>", "()V", "onCreate", "", "createNotificationChannel", "configureStrictMode", "app_debug"})
public final class ArmadaApplication extends android.app.Application {
    
    public ArmadaApplication() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    private final void createNotificationChannel() {
    }
    
    private final void configureStrictMode() {
    }
}