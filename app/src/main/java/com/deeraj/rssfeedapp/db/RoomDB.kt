package com.deeraj.rssfeedapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deeraj.rssfeedapp.db.schemas.RssFeed
import com.deeraj.rssfeedapp.db.schemas.SavedString
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [SavedString::class, RssFeed::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stringDao(): StringDao
    abstract fun rssDao(): RssDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        val passphrase: ByteArray = SQLiteDatabase.getBytes("deeraj928929".toCharArray())
        val factory = SupportFactory(passphrase)

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).openHelperFactory(factory)
                    .fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
