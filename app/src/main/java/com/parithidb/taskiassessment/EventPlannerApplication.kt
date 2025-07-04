package com.parithidb.taskiassessment

import android.app.Application
import com.parithidb.taskiassessment.util.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EventPlannerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }
}