package com.example.ourmatterz.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.ourmatterz.R
import com.example.ourmatterz.data.categories
import com.example.ourmatterz.data.models.Article
import com.example.ourmatterz.viewmodel.MatterzViewModel


@Composable
fun CategoriesBar(
    viewModel: MatterzViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.currentState.collectAsState()

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(color = CardDefaults.cardColors().containerColor, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        items(items = categories, key = {it.first}) { item ->
            Row {
                VerticalDivider(color = Color.Gray, modifier = Modifier.height(26.dp).width(2.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = item.first,
                    color = if (selectedCategory.selectedCategory == item.second) Color.Red else Color.Unspecified,
                    modifier = Modifier
                        .clickable(onClick = {
                            viewModel.updateSelectedCategory(item.second)
                            viewModel.getArticles()
                        })
                )
                Spacer(Modifier.width(4.dp))
                VerticalDivider(color = Color.Gray, modifier = Modifier.height(26.dp).width(2.dp))
            }
        }
    }
}


@Composable
fun SearchFAB(
    //clickState: Boolean,
    onClick: (Boolean) -> Unit,
    viewModel: MatterzViewModel,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var state by rememberSaveable { mutableStateOf(false) }
    val queryState by viewModel.currentState.collectAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .fillMaxWidth()
    ) {

        AnimatedVisibility(visible = state) {
            TextField(
                value = queryState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search articles") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = {
                    keyboardController?.hide()
                    onClick(state)
                    viewModel.searchArticles()
                    viewModel.updateSearchQuery("")
                }),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(width = 4.dp, brush = Brush.verticalGradient(listOf(Color.Gray, Color.White)), shape = RoundedCornerShape(16.dp))
            )
        }

        Spacer(Modifier.width(16.dp))

        FloatingActionButton(
            shape = RoundedCornerShape(50.dp),
            onClick = {
                state = !state
                onClick(state)
            },
            modifier = Modifier.border(width = 4.dp, color = Color.Gray, shape = RoundedCornerShape(50.dp))
        ) {
            Icon(
                imageVector = if (state) Icons.Default.Close else Icons.Default.Search,
                contentDescription = null
            )
        }
    }
}


@Composable
fun ArticleCard(
    article: Article,
    onClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: MatterzViewModel = viewModel(factory = MatterzViewModel.Factory)
    val bookmarks by viewModel.bookmarks.collectAsState()

    Card(
        onClick = { onClick(article) },
        modifier = modifier
            .height(220.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "Article Image",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_background),
                modifier = Modifier.fillMaxSize()
            )

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.DarkGray
                            )
                        ), shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.DarkGray
                                    )
                                ), shape = RoundedCornerShape(8.dp)
                            )
                            .widthIn(max = 200.dp)
                    ) {
                        Text(
                            text = article.source?.name ?: "",
                            color = Color.White,
                            maxLines = 1
                        )
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .background(color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = if (bookmarks.contains(article)) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.clickable(
                                onClick = {
                                    if (bookmarks.contains(article)) viewModel.removeBookmark(article) else viewModel.addBookmark(article)
                                }
                            )
                        )
                    }

                }

                Spacer(modifier = Modifier)

                Text(
                    text = article.title ?: "",
                    style = TextStyle(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White,
                                Color.LightGray
                            )
                        ),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.padding(8.dp)
                )

//                Spacer(modifier = Modifier)
//
//                Text(
//                    text = article.description ?: ""
//                )
            }
        }
    }
}