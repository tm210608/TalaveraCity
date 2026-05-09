package com.example.eboraazule.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AzulejoDao {
    @Query("SELECT * FROM azulejos_coleccionados ORDER BY coleccionadoEn DESC")
    fun getAllAzulejos(): Flow<List<AzulejoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAzulejo(azulejo: AzulejoEntity)

    @Delete
    suspend fun deleteAzulejo(azulejo: AzulejoEntity)

    @Query("SELECT EXISTS(SELECT * FROM azulejos_coleccionados WHERE id = :id)")
    suspend fun isAzulejoColeccionado(id: String): Boolean
}
