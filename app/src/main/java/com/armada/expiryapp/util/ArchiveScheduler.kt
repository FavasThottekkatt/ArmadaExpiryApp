package com.armada.expiryapp.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.armada.expiryapp.worker.ArchiveWorker
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

object ArchiveScheduler {

    const val WORK_NAME = "monthly_archive_job"

    fun scheduleIfNeeded(context: Context) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                buildRequest(),
            )
    }

    fun reschedule(context: Context) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                buildRequest(),
            )
    }

    private fun buildRequest() =
        PeriodicWorkRequestBuilder<ArchiveWorker>(30, TimeUnit.DAYS)
            .setInitialDelay(msToNextArchive(), TimeUnit.MILLISECONDS)
            .addTag(WORK_NAME)
            .build()

    // Calculates milliseconds until the next 1st of the month at midnight local time.
    private fun msToNextArchive(): Long {
        val now = LocalDateTime.now()
        var target = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        if (!now.isBefore(target)) target = target.plusMonths(1)
        return ChronoUnit.MILLIS.between(now, target).coerceAtLeast(0L)
    }
}
