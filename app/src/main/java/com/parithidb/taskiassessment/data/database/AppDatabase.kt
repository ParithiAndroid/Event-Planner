package com.parithidb.taskiassessment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.parithidb.taskiassessment.data.database.dao.EventDao
import com.parithidb.taskiassessment.data.database.entities.EventEntity

@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    fun clearDatabase() {
        clearAllTables()
    }
}