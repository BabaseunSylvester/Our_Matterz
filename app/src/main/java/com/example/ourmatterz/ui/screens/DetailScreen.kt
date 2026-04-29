package com.example.ourmatterz.ui.screens

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.ourmatterz.R
import com.example.ourmatterz.data.models.Article

@Composable
fun DetailScreen(
    article: Article?,
    onBack: () -> Unit,
    onContinueReading: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailImage(
            onBack = onBack,
            article = article
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = article?.title ?: "",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(8.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = article?.content ?: "",
            lineHeight = TextUnit(30f, TextUnitType.Sp),
            style = TextStyle(
                brush = Brush.verticalGradient(listOf(Color.White, Color.LightGray))
            ),
            modifier = Modifier.padding(16.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(article?.url))
                onContinueReading()
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Read Full Article")
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Default.ArrowForward, null)
            }
        }
    }
}


@Composable
fun DetailImage(
    onBack: () -> Unit,
    article: Article?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        Color.DarkGray
                    )
                )
            )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article?.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "Article Image",
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_background),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .background(color = Color.Gray)
                ) {
                    IconButton(
                        onClick = onBack
                    ) { Icon(Icons.Default.ArrowBack, null) }
                }
            }
        }
    }
}