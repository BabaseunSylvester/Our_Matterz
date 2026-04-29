package com.example.ourmatterz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ourmatterz.ui.screens.HomeScreen
import com.example.ourmatterz.ui.screens.MatterzNav
import com.example.ourmatterz.ui.theme.OurMatterzTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OurMatterzTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MatterzNav(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

