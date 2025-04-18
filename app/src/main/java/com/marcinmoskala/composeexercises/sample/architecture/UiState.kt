@file:Suppress("unused")

package com.marcinmoskala.composeexercises.sample.architecture

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsViewModel {
    private val _news = MutableStateFlow(emptyList<News>())
    val news = _news.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
}

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val loading = viewModel.loading.collectAsStateWithLifecycle()
    val news = viewModel.news.collectAsStateWithLifecycle()

    if (loading.value) {
        ProgressScreen()
    } else {
        NewsList(news.value)
    }
}

//class NewsViewModel {
//    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
//    val uiState = _uiState.asStateFlow()
//}
//
//@Composable
//fun NewsScreen(viewModel: NewsViewModel) {
//    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
//
//    when (val state = uiState.value) {
//        NewsUiState.Loading -> ProgressScreen()
//        is NewsUiState.ShowNews -> NewsList(state.news)
//    }
//}
//
//sealed interface NewsUiState {
//    data object Loading : NewsUiState
//    data class ShowNews(val news: List<News>) : NewsUiState
//}

data class News(
    val title: String,
    val content: String,
    val imageUrl: String,
)

@Composable
fun ProgressScreen() {
    // Show progress
}

@Composable
fun NewsList(news: List<News>) {
    // Show news
}