package com.armada.expiryapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.armada.expiryapp.data.repository.ExpiryEntryRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class ArchiveWorker(
    private val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ArchiveWorkerEntryPoint {
        fun expiryEntryRepository(): ExpiryEntryRepository
    }

    override suspend fun doWork(): Result {
        return try {
            val repo = EntryPointAccessors.fromApplication(
                context.applicationContext,
                ArchiveWorkerEntryPoint::class.java,
            ).expiryEntryRepository()
            repo.archiveAllActive()
            Log.i(TAG, "Monthly archive completed successfully.")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Monthly archive failed: ${e.message}", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "ArchiveWorker"
    }
}
