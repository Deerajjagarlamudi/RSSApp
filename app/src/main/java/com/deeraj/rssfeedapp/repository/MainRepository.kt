package com.deeraj.rssfeedapp.repository

import com.deeraj.rssfeedapp.utils.XMLParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepository {
    // This function use for fetch articles from server without blocking thread
    fun fetchArticles(url: String) = flow {
        emit(XMLParser.parseRssWithJsoup(url))
    }.flowOn(Dispatchers.IO)
}