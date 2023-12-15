package com.jask.pomotrack.system

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.jask.pomotrack.R
import kotlin.random.Random

class NotificationService(private val context: Context) {
    fun showBasicNotification() {
        val notification = NotificationCompat.Builder(context, "timer_notification")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("timer")
            .setContentText("timer")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Dissapears after clicking the notification
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(Random.nextInt(), notification)
    }
}