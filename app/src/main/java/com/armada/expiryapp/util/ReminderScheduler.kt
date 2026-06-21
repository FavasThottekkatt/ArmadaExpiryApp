package com.armada.expiryapp.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.armada.expiryapp.worker.ReminderWorker
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    const val WORK_NAME = "armada_monthly_reminder"

    // Called on first app start — keeps any existing schedule intact.
    fun scheduleIfNeeded(context: Context) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                buildRequest(),
            )
    }

    // Called from BootReceiver — always recalculates the initial delay after restart.
    fun reschedule(context: Context) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                buildRequest(),
            )
    }

    private fun buildRequest() =
        PeriodicWorkRequestBuilder<ReminderWorker>(30, TimeUnit.DAYS)
            .setInitialDelay(msToNextReminder(), TimeUnit.MILLISECONDS)
            .addTag(WORK_NAME)
            .build()

    // Calculates milliseconds until the next 18th of the month at 09:00 local time.
    private fun msToNextReminder(): Long {
        val now = LocalDateTime.now()
        var target = now
            .withDayOfMonth(18)
            .withHour(9)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        if (!now.isBefore(target)) target = target.plusMonths(1)
        return ChronoUnit.MILLIS.between(now, target).coerceAtLeast(0L)
    }
}
