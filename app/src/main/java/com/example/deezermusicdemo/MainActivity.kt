package com.example.deezermusicdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.deezermusicdemo.ui.screen.MainScreen
import com.example.deezermusicdemo.ui.theme.DeezerMusicDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeezerMusicDemoTheme {
                MainScreen()
            }
        }
    }
}
