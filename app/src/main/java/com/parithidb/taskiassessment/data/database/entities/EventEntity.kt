package com.parithidb.taskiassessment.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EVENTS")
class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("eventId")
    val eventId: Int = 0,
    @ColumnInfo("eventTitle")
    val eventTitle: String,
    @ColumnInfo("eventDescription")
    val eventDescription: String,
    @ColumnInfo("eventDateAndTime")
    val eventDateAndTime: Long,
    @ColumnInfo("isAlarmEnabled")
    val isAlarmEnabled: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventEntity

        if (eventId != other.eventId) return false
        if (eventTitle != other.eventTitle) return false
        if (eventDescription != other.eventDescription) return false
        if (eventDateAndTime != other.eventDateAndTime) return false
        if (isAlarmEnabled != other.isAlarmEnabled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = eventId
        result = 31 * result + eventTitle.hashCode()
        result = 31 * result + eventDescription.hashCode()
        result = 31 * result + eventDateAndTime.hashCode()
        result = 31 * result + isAlarmEnabled.hashCode()
        return result
    }
}
