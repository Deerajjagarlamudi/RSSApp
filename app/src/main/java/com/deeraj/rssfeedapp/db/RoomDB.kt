package com.deeraj.rssfeedapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deeraj.rssfeedapp.db.schemas.RssFeed
import com.deeraj.rssfeedapp.db.schemas.SavedString

@Database(entities = [SavedString::class, RssFeed::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stringDao(): StringDao
    abstract fun rssDao(): RssDao
}
