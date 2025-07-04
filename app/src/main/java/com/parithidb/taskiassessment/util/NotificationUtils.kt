package com.parithidb.taskiassessment.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.parithidb.taskiassessment.data.database.entities.EventEntity
import com.parithidb.taskiassessment.ui.reminder.EventReminderReceiver

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "event_channel",
            "Event Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for event reminders"
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

fun scheduleEventReminder(context: Context, event: EventEntity) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            return
        }
    }

    val intent = Intent(context, EventReminderReceiver::class.java).apply {
        putExtra("title", event.eventTitle)
        putExtra("description", event.eventDescription)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        event.eventId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    try {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            event.eventDateAndTime,
            pendingIntent
        )
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}

fun cancelEventReminder(context: Context, eventId: Int) {
    val intent = Intent(context, EventReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        eventId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}



