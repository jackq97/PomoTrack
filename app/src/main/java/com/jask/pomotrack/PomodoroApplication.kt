package com.jask.pomotrack

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PomodoroApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        val notificationChannel = NotificationChannel(
            "timer_notification", // id
            "timer", // name
            NotificationManager.IMPORTANCE_HIGH // importance
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}