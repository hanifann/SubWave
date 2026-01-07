package com.hanifan.subwave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.hanifan.subwave.navigation.AppNavigation
import com.hanifan.subwave.ui.theme.SubWaveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(it)
                .crossfade(true)
                .build()
        }

        enableEdgeToEdge()
        setContent {
            SubWaveTheme {
                AppNavigation()
            }
        }
    }
}