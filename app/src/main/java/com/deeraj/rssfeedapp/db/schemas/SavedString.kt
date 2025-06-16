package com.deeraj.rssfeedapp.db.schemas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strings")
data class SavedString(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: String
)

