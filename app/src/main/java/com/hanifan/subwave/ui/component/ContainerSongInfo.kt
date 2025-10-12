package com.hanifan.subwave.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun ContainerSongInfo(
    songTitle: String,
    singer: String,
    albumArt: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .wrapContentSize()
            .sizeIn(maxHeight = 230.dp, maxWidth = 200.dp)
            .clickable(
                onClick = onClick
            )
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = albumArt,
                contentDescription = "album art",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 200.dp, height = 160.dp)
                    .padding(bottom = 12.dp)
                    .clip(shape = RoundedCornerShape(size = 12.dp))
            )
            SongInfoText(
                text = songTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            SongInfoText(
                text = singer,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun SongInfoText(
    text: String,
    fontSize: TextUnit,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        fontWeight = fontWeight,
        fontSize = fontSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}