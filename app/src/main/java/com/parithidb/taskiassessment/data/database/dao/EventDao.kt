package com.parithidb.taskiassessment.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.parithidb.taskiassessment.data.database.entities.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventEntity: EventEntity)

    @Query("SELECT * FROM EVENTS WHERE eventDateAndTime >= :currentTime ORDER BY eventDateAndTime ASC")
    fun getUpcomingEvents(currentTime: Long = System.currentTimeMillis()): LiveData<List<EventEntity>>

    @Query("SELECT * FROM EVENTS WHERE eventDateAndTime BETWEEN :startOfDay AND :endOfDay ORDER BY eventDateAndTime ASC")
    fun getEventsByDate(startOfDay: Long, endOfDay: Long): LiveData<List<EventEntity>>

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM EVENTS WHERE eventId = :eventId")
    suspend fun deleteEventById(eventId: Int)

    @Query("UPDATE EVENTS SET isAlarmEnabled = :isEnabled WHERE eventId = :eventId")
    suspend fun updateAlarmStatus(eventId: Int, isEnabled: Boolean)


}