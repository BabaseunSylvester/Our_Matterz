package com.example.ourmatterz.ui.screens

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ourmatterz.viewmodel.MatterzViewModel


enum class Screens {
    HOME, DETAIL, BOOKMARKS
}

@Composable
fun MatterzNav(
    modifier: Modifier = Modifier
) {
    val viewModel: MatterzViewModel = viewModel(factory = MatterzViewModel.Factory)
    val currentViewArticle by viewModel.currentViewArticleState.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.HOME.name,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Screens.HOME.name) {
            HomeScreen(
                viewModel = viewModel,
                onArticleClick = {
                    viewModel.updateCurrentViewArticle(it)
                    navController.navigate(Screens.DETAIL.name)
                },
                onGoToBookmarks = { navController.navigate(Screens.BOOKMARKS.name) }
            )
        }
        composable(Screens.DETAIL.name) {
            DetailScreen(
                article = currentViewArticle.article,
                onBack = { navController.popBackStack() },
                onContinueReading = { /* TODO */ }
            )
        }
        composable(
            Screens.BOOKMARKS.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
            }
        ) {
            BookmarksScreen(
                articles = bookmarks.reversed(),
                onBack = { navController.popBackStack() },
                onArticleClick = {
                    viewModel.updateCurrentViewArticle(it)
                    navController.navigate(Screens.DETAIL.name)
                }
            )
        }
    }
}