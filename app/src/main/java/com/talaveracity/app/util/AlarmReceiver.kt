package com.talaveracity.app.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Evento Cultural"
        val message = intent.getStringExtra("message") ?: "¡Tu evento está por comenzar!"
        
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification(title, message)
    }
}




