package com.example.ourmatterz.viewmodel

import com.example.ourmatterz.data.models.Article

data class CurrentUiState(
    val selectedCategory: String = "general",
    val searchQuery: String = ""
)

data class CurrentDetailViewState(
    val article: Article? = null
)

sealed interface NewsUiState {
    data class Success(val articles: List<Article>) : NewsUiState
    object NoArticlesFound : NewsUiState
    object Loading : NewsUiState
    object Error : NewsUiState
}