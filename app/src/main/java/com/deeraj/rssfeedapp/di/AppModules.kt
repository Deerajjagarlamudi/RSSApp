package com.deeraj.rssfeedapp.di

import android.content.Context
import androidx.room.Room
import com.deeraj.rssfeedapp.db.AppDatabase
import com.deeraj.rssfeedapp.db.RssDao
import com.deeraj.rssfeedapp.db.SecureDatabaseManager
import com.deeraj.rssfeedapp.db.StringDao
import com.deeraj.rssfeedapp.repositories.MainRepository
import com.deeraj.rssfeedapp.repositories.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModules {


    @Provides
    fun provideMainRepository() = MainRepository()


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val encryptionFactory = SecureDatabaseManager.getSecureDatabaseFactory(context)
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        ).openHelperFactory(encryptionFactory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideStringDao(database: AppDatabase): StringDao {
        return database.stringDao()
    }


    @Provides
    fun provideRssDao(database: AppDatabase): RssDao {
        return database.rssDao()
    }

    @Provides
    fun provideRoomRepository(dao: StringDao,rssDao: RssDao): RoomRepository {
        return RoomRepository(dao,rssDao)
    }

}