package com.deeraj.rssfeedapp.models

data class RssItem(
    val title: String,
    val description: String,
    val pubDate: String,
    val link: String,
    val imageUrl: String? = null,
    val interests: String?=null,
    val category: String?=null,
    val location: String?= null
)