package com.example.eboraazule.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedEventEntity::class], version = 1, exportSchema = false)
abstract class EboraDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
