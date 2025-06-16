package com.deeraj.rssfeedapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deeraj.rssfeedapp.db.schemas.RssFeed
import kotlinx.coroutines.flow.Flow

@Dao
interface RssDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RssFeed>)


    @Query("SELECT * FROM rssfeed")
    fun getAllRSSFeed(): Flow<List<RssFeed>>


    @Query("DELETE FROM rssfeed WHERE id = :id")
    suspend fun deleteRSSFeedById(id: Int)

    // Delete feeds older than 30 days (in millis)
    @Query("DELETE FROM rssfeed WHERE timestamp < :cutoffTime")
    suspend fun deleteFeedsOlderThan(cutoffTime: Long)


}
