package com.example.ourmatterz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ourmatterz.MatterzApplication
import com.example.ourmatterz.data.Repository
import com.example.ourmatterz.data.models.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MatterzViewModel(private val repository: Repository) : ViewModel() {

    private val _currentState = MutableStateFlow(CurrentUiState())
    val currentState = _currentState.asStateFlow()

    private val _currentViewArticleState = MutableStateFlow(CurrentDetailViewState())
    val currentViewArticleState = _currentViewArticleState.asStateFlow()

    private val _newsState: MutableStateFlow<NewsUiState> = MutableStateFlow(NewsUiState.Loading)
    val newsState = _newsState.asStateFlow()


    init {
        getArticles()
    }


    fun updateSearchQuery(searchQuery: String) {
        _currentState.update {
            it.copy(searchQuery = searchQuery)
        }
    }


    fun updateSelectedCategory(category: String) {
        _currentState.update {
            it.copy(selectedCategory = category)
        }
    }


    fun updateCurrentViewArticle(article: Article) {
        viewModelScope.launch {
            _currentViewArticleState.update {
                it.copy(article = article)
            }
        }
    }


    fun searchArticles() {
        viewModelScope.launch {
            _newsState.value = NewsUiState.Loading

            _newsState.value = try {
                val result = repository.searchNews(searchQuery = _currentState.value.searchQuery)
                if (result == null) {
                    NewsUiState.Error
                } else if (result.isEmpty()) {
                    NewsUiState.NoArticlesFound
                } else {
                    NewsUiState.Success(result)
                }
            } catch (e: Exception) {
                NewsUiState.Error
            } catch (e: IOException) {
                NewsUiState.Error
            } catch (e: HttpException) {
                NewsUiState.Error
            }
        }
    }


    fun getArticles() {
        viewModelScope.launch {
            _newsState.value = NewsUiState.Loading

            _newsState.value = try {
                val result = repository.getNews(category = _currentState.value.selectedCategory)
                if (result == null) {
                    NewsUiState.Error
                } else if (result.isEmpty()) {
                    NewsUiState.NoArticlesFound
                } else {
                    NewsUiState.Success(result)
                }
            } catch (e: Exception) {
                NewsUiState.Error
            } catch (e: IOException) {
                NewsUiState.Error
            } catch (e: HttpException) {
                NewsUiState.Error
            }
        }
    }



    val bookmarks: StateFlow<List<Article>> = repository.getArticles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeBookmark(article: Article) = viewModelScope.launch { repository.deleteArticle(article) }
    fun addBookmark(article: Article) = viewModelScope.launch { repository.addArticle(article) }




    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as MatterzApplication
                MatterzViewModel(application.container.repository)
            }
        }
    }
}