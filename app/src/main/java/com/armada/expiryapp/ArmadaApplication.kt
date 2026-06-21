package com.armada.expiryapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.StrictMode
import com.armada.expiryapp.util.ArchiveScheduler
import com.armada.expiryapp.util.ArmadaCrashHandler
import com.armada.expiryapp.util.ReminderScheduler
import com.armada.expiryapp.worker.ReminderWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArmadaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(ArmadaCrashHandler(this))
        createNotificationChannel()
        ReminderScheduler.scheduleIfNeeded(this)
        ArchiveScheduler.scheduleIfNeeded(this)
        configureStrictMode()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            ReminderWorker.CHANNEL_ID,
            "Expiry Report Reminders",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Monthly reminder to submit expiry reports before the 20th."
        }
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private fun configureStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()   // Log only — Samsung IdsController reads disk on main thread during onResume
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }
    }
}
