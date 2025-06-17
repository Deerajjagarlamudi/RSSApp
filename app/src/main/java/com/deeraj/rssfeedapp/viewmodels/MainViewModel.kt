package com.deeraj.rssfeedapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deeraj.rssfeedapp.models.RssItem
import com.deeraj.rssfeedapp.repository.MainRepository
import com.deeraj.rssfeedapp.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val mainRepository: MainRepository) : ViewModel() {


   private val _articles: MutableStateFlow<Resources<List<RssItem>>> = MutableStateFlow(Resources.Empty())
    val articles = _articles.asStateFlow()

    fun getArticles(url: String) = viewModelScope.launch {
        _articles.emit(Resources.Loading())
        mainRepository.fetchArticles(url).catch {
            _articles.emit(Resources.Error(it.localizedMessage))
        }.collectLatest {
            _articles.emit(Resources.Success(it))
        }
    }
}