package com.example.eboraazule.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedEventEntity::class, AzulejoEntity::class], version = 2, exportSchema = false)
abstract class EboraDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun azulejoDao(): AzulejoDao
}
