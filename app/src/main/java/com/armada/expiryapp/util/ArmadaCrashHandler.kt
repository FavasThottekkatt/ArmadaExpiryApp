package com.armada.expiryapp.util

import android.content.Context
import android.os.Build
import com.armada.expiryapp.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArmadaCrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            val logDir = context.getExternalFilesDir("logs")
            if (logDir != null) {
                logDir.mkdirs()
                val logFile = File(logDir, "crash_log.txt")
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val entry = """
                    Timestamp: $timestamp
                    Device: ${Build.MANUFACTURER} ${Build.MODEL}
                    Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})
                    App version: ${BuildConfig.VERSION_NAME}
                    Thread: ${thread.name}
                    ---STACKTRACE---
                    ${throwable.stackTraceToString()}
                    ================
                """.trimIndent()
                logFile.appendText(entry + "\n\n")
            }
        } catch (e: Exception) {
            // Never cause a crash loop — swallow silently here only
        }
        defaultHandler?.uncaughtException(thread, throwable)
    }
}
