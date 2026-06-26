package com.talaveracity.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SavedEventEntity::class, 
        CachedEventEntity::class, 
        CeramicPieceEntity::class,
        OfficialInfoEntity::class
    ], 
    version = 6, 
    exportSchema = false
)
abstract class TalaveraCityDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}




