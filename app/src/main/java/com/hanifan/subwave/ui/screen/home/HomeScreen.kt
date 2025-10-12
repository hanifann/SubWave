package com.hanifan.subwave.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hanifan.subwave.navigation.Routes
import com.hanifan.subwave.ui.component.ContainerSongInfo
import com.hanifan.subwave.ui.component.SectionHeader

@Composable
fun HomeScreen(
    onNavigate: (route: Routes) -> Unit
) {
    Scaffold { contentPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
        ) {
            SongSection(
                title = "Recommended",
                songTitle = "Scene six: through her eyes",
                singer = "Dream Theatre",
                albumArt = "https://picsum.photos/400"
            ) {
                onNavigate(Routes.SectionDetailRoute)
            }
            SongSection(
                title = "Recently Played",
                songTitle = "Scene six: through her eyes",
                singer = "Dream Theatre",
                albumArt = "https://picsum.photos/400"
            ) {

            }
            SongSection(
                title = "Most Played",
                songTitle = "Scene six: through her eyes",
                singer = "Dream Theatre",
                albumArt = "https://picsum.photos/400"
            ) {

            }
            SongSection(
                title = "Recommended",
                songTitle = "Scene six: through her eyes",
                singer = "Dream Theatre",
                albumArt = "https://picsum.photos/400"
            ) {

            }
            Spacer(
                modifier = Modifier
                    .height(height = contentPadding.calculateBottomPadding())
            )
        }
    }
}

@Composable
private fun SongSection(
    title: String,
    songTitle: String,
    singer: String,
    albumArt: String,
    onClick: () -> Unit,

) {
    SectionHeader(
        title = title,
        modifier = Modifier
            .padding(vertical = 24.dp, horizontal = 16.dp)
    )
    LazyRow (
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(10) {
            ContainerSongInfo(
                songTitle = songTitle,
                singer = singer,
                albumArt = albumArt,
                onClick = onClick,
            )
        }
    }
}