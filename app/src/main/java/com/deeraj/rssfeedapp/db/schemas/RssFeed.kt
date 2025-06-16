package com.deeraj.rssfeedapp.db.schemas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rssfeed")
data class RssFeed(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val pubDate: String,
    val link: String,
    val identifierUrl: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null,
    val interests: String?=null,
    val category: String?=null,
    val location: String?=null

)