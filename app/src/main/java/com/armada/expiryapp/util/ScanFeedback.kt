package com.armada.expiryapp.util

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

object ScanFeedback {
    fun play(view: View) {
        val constant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            HapticFeedbackConstants.CONFIRM
        else
            HapticFeedbackConstants.VIRTUAL_KEY
        view.performHapticFeedback(constant)

        try {
            val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60)
            tg.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
            view.postDelayed({ tg.release() }, 300)
        } catch (_: Exception) {}
    }
}
