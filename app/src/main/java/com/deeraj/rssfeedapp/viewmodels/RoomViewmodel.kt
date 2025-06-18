package com.deeraj.rssfeedapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deeraj.rssfeedapp.db.schemas.RssFeed
import com.deeraj.rssfeedapp.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewmodel @Inject constructor(val roomRepository: RoomRepository) : ViewModel() {


    val getAllStrings = roomRepository.allStrings.stateIn(
        viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList()
    )
    val getAllArticles= roomRepository.getAllArticles.stateIn(
        viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList()
    )

    fun saveString(stringUrl: String) = viewModelScope.launch {
        roomRepository.saveString(stringUrl)
    }


    fun deleteString(id: Int) = viewModelScope.launch {
        roomRepository.deleteString(id)
    }


    fun deleteOldArticles() = viewModelScope.launch {
        roomRepository.cleanupOldFeeds()
    }


    fun saveArticles(articles: List<RssFeed>) = viewModelScope.launch {
        roomRepository.saveArticlesInDB(articles)
    }


}