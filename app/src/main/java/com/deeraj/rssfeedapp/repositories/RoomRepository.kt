package com.deeraj.rssfeedapp.repositories

import com.deeraj.rssfeedapp.db.RssDao
import com.deeraj.rssfeedapp.db.StringDao
import com.deeraj.rssfeedapp.db.schemas.RssFeed
import com.deeraj.rssfeedapp.db.schemas.SavedString
import kotlinx.coroutines.flow.Flow

class RoomRepository(private val dao: StringDao, private val rssDao: RssDao) {


    val allStrings: Flow<List<SavedString>> = dao.getAllStrings()

    val getAllArticles: Flow<List<RssFeed>> = rssDao.getAllRSSFeed()

    suspend fun saveString(value: String) {
        dao.insertString(SavedString(value = value))
    }


    suspend fun deleteString(id: Int) {
        dao.deleteStringById(id)
    }

    suspend fun cleanupOldFeeds() {
        val thirtyDaysAgo = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
        rssDao.deleteFeedsOlderThan(thirtyDaysAgo)
    }


    suspend fun saveArticlesInDB(items: List<RssFeed>) {
        rssDao.insertAll(items)
    }


}