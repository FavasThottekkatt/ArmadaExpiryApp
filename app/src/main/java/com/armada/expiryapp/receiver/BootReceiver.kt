package com.armada.expiryapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.armada.expiryapp.util.ArchiveScheduler
import com.armada.expiryapp.util.ReminderScheduler

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            ReminderScheduler.reschedule(context)
            ArchiveScheduler.reschedule(context)
        }
    }
}
