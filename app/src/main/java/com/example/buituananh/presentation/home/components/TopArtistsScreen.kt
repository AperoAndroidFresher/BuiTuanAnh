package com.example.buituananh.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.domain.model.Artist
import com.example.buituananh.domain.model.Track
import com.example.buituananh.presentation.components.BackTopBar

@Composable
fun TopArtistScreen(
    popBack: () -> Unit,
    modifier: Modifier = Modifier,
    artists: List<Artist> = emptyList()
) {

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.top_tracks),
                popBack = popBack
            )
        },
        modifier = modifier
    ) {
        LazyVerticalGrid (
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(artists) { artist ->
                ArtistItem(artist = artist)
            }
        }
    }

}
