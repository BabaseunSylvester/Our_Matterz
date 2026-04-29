package com.example.ourmatterz.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ourmatterz.data.models.Article
import com.example.ourmatterz.viewmodel.MatterzViewModel
import com.example.ourmatterz.viewmodel.NewsUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MatterzViewModel,
    onArticleClick: (Article) -> Unit,
    onGoToBookmarks: () -> Unit,
    modifier: Modifier = Modifier
) {
    val newsState = viewModel.newsState.collectAsState().value
    var fabClickState by rememberSaveable { mutableStateOf(false) }
    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val state = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp).fillMaxWidth()
                    ) {
                        Text("World News", fontWeight = FontWeight.Bold, fontSize = 26.sp, style = TextStyle(brush = Brush.linearGradient(listOf(Color.Red, Color.White))))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.wrapContentSize()
                                .padding(end = 8.dp)
                                .clickable(onClick = onGoToBookmarks)
                        ) {
                            Text("Bookmarks", fontSize = 14.sp, modifier = Modifier.padding(horizontal = 1.dp))
                            Icon(Icons.Default.Bookmarks, null)
                        }
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                expandedHeight = 36.dp
            )
        },
        floatingActionButton = { SearchFAB(onClick = {fabClickState = it}, viewModel = viewModel) },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            //verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding)
        ) {
            AnimatedVisibility(visible = !fabClickState) {
                CategoriesBar(
                    viewModel = viewModel,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(Modifier.height(4.dp))

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                state = state,
                onRefresh = { if (fabClickState) viewModel.searchArticles() else viewModel.getArticles() }
            ) {
                when (newsState) {
                    is NewsUiState.NoArticlesFound -> NoArticlesScreen()
                    is NewsUiState.Error -> ErrorScreen({ viewModel.searchArticles() })
                    is NewsUiState.Loading -> LoadingScreen()
                    is NewsUiState.Success -> SuccessScreen(
                        articles = newsState.articles,
                        onArticleClick = { onArticleClick(it) })
                }
            }

        }

    }

}



@Composable
fun SuccessScreen(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
    ) {
        items(items = articles) {
            ArticleCard(
                article = it,
                onClick = { item -> onArticleClick(item) }
            )
        }
    }

}


@Composable
fun ErrorScreen(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Error loading content!",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetryClick
        ) { Text("Retry") }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text("Fetching News...")
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator()
    }
}

@Composable
fun NoArticlesScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text("Sorry! No news report found.")
    }
}