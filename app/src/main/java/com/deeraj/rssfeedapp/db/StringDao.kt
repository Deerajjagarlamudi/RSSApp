package com.deeraj.rssfeedapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deeraj.rssfeedapp.db.schemas.SavedString
import kotlinx.coroutines.flow.Flow

@Dao
interface StringDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertString(item: SavedString)

    @Query("SELECT * FROM strings")
    fun getAllStrings(): Flow<List<SavedString>>


    @Query("DELETE FROM strings WHERE id = :id")
    suspend fun deleteStringById(id: Int)
}
