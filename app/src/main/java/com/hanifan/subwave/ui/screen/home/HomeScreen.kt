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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hanifan.subwave.domain.home.enum.SongCategory
import com.hanifan.subwave.domain.home.model.Album
import com.hanifan.subwave.navigation.Routes
import com.hanifan.subwave.ui.component.ContainerSongInfo
import com.hanifan.subwave.ui.component.SectionHeader

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (Routes) -> Unit,
) {
    val mostPlayedState by homeViewModel.mostPlayedState.collectAsStateWithLifecycle()
    val recommendedState by homeViewModel.recommendedAlbumState.collectAsStateWithLifecycle()
    val newestState by homeViewModel.newestAlbumState.collectAsStateWithLifecycle()
    val recentlyAddedState by homeViewModel.recentlyAddedAlbumState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        homeViewModel.getTopAlbum()
        homeViewModel.getRecommendedAlbum()
        homeViewModel.getRecentlyAddedAlbum()
        homeViewModel.getRecentlyPlayedAlbum()
    }

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
        ) {
            when {
                mostPlayedState.isLoading -> {
                    CircularProgressIndicator()
                }

                mostPlayedState.errorMessage.isNotBlank() -> {
                    Text(mostPlayedState.errorMessage)
                }

                mostPlayedState.data.isNotEmpty() -> {
                    SongSection(
                        title = SongCategory.MOSTPLAYED.displayName,
                        data = mostPlayedState.data,
                        coverArtUrl = homeViewModel::buildCoverArtUrl,
                    ) {
                        onNavigate(Routes.AlbumRoute)
                    }
                }
            }
            when {
                newestState.isLoading -> {
                    CircularProgressIndicator()
                }

                newestState.errorMessage.isNotBlank() -> {
                    Text(newestState.errorMessage)
                }

                newestState.data.isNotEmpty() -> {
                    SongSection(
                        title = SongCategory.RECENTLYADDED.displayName,
                        data = newestState.data,
                        coverArtUrl = homeViewModel::buildCoverArtUrl,
                    ) {
                        onNavigate(Routes.AlbumRoute)
                    }
                }
            }
            when {
                recentlyAddedState.isLoading -> {
                    CircularProgressIndicator()
                }

                recentlyAddedState.errorMessage.isNotBlank() -> {
                    Text(recentlyAddedState.errorMessage)
                }

                recentlyAddedState.data.isNotEmpty() -> {
                    SongSection(
                        title = SongCategory.RECENTLYPLAYED.displayName,
                        data = recentlyAddedState.data,
                        coverArtUrl = homeViewModel::buildCoverArtUrl,
                    ) {
                        onNavigate(Routes.AlbumRoute)
                    }
                }
            }
            when {
                recommendedState.isLoading -> {
                    CircularProgressIndicator()
                }

                recommendedState.errorMessage.isNotBlank() -> {
                    Text(recommendedState.errorMessage)
                }

                recommendedState.data.isNotEmpty() -> {
                    SongSection(
                        title = SongCategory.RECOMMENDED.displayName,
                        data = recommendedState.data,
                        coverArtUrl = homeViewModel::buildCoverArtUrl,
                    ) {
                        onNavigate(Routes.AlbumRoute)
                    }
                }
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
    data: List<Album>,
    coverArtUrl: (String)-> String,
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
        items(data.size) {index ->
            ContainerSongInfo(
                songTitle = data[index].title,
                singer = data[index].artist,
                albumArt = coverArtUrl(data[index].coverArt),
                onClick = onClick,
            )
        }
    }
}