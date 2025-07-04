package com.parithidb.taskiassessment.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.parithidb.taskiassessment.data.database.AppDatabase
import com.parithidb.taskiassessment.data.database.dao.EventDao
import com.parithidb.taskiassessment.data.database.entities.EventEntity
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val context: Context,
    private val database: AppDatabase,
    private val eventDao: EventDao = database.eventDao()
) {
    suspend fun insertEvent(eventEntity: EventEntity) {
        eventDao.insertEvent(eventEntity)
    }

    fun getUpcomingEvents(): LiveData<List<EventEntity>> {
        return eventDao.getUpcomingEvents()
    }

    fun getEventsByDate(startOfDay: Long, endOfDay: Long): LiveData<List<EventEntity>> {
        return eventDao.getEventsByDate(startOfDay, endOfDay)
    }

    suspend fun updateEvent(event: EventEntity) {
        eventDao.updateEvent(event)
    }

    suspend fun deleteEventById(eventId: Int) {
        eventDao.deleteEventById(eventId)
    }

    suspend fun updateAlarmStatus(eventId: Int, isEnabled: Boolean) {
        eventDao.updateAlarmStatus(eventId, isEnabled)
    }


}