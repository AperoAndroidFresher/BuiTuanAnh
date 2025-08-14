package com.example.buituananh.presentation.home.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.R
import com.example.buituananh.domain.model.Album
import com.example.buituananh.domain.model.Artist
import com.example.buituananh.domain.model.Track
import com.example.buituananh.presentation.components.LoadingAnimation
import com.example.buituananh.presentation.home.HomeEffect
import com.example.buituananh.presentation.home.HomeIntent
import com.example.buituananh.presentation.home.HomeState
import com.example.buituananh.presentation.home.HomeViewModel
import com.example.buituananh.presentation.library.component.NoInternetSection
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.Destination
import com.example.buituananh.util.identifyIndexToColor

@Composable
fun HomeScreenRoot(
    onNavigate: (Destination) -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToProfileScreen -> onNavigate(Destination.ProfileScreen)
                HomeEffect.NavigateToSettingScreen -> onNavigate(Destination.SettingScreen)
                is HomeEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
                is HomeEffect.NavigateToAlbumsScreen -> onNavigate(Destination.AlbumsScreen(effect.albums))
                is HomeEffect.NavigateToArtistScreen -> onNavigate(Destination.ArtistScreen(effect.artist))
                is HomeEffect.NavigateToTracksScreen -> onNavigate(Destination.TrackScreen(effect.tracks))
            }
        }
    }
    
    HomeScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        topBar = {
            AvatarTopBar(
                clickSetting = {
                    onIntent(HomeIntent.ClickSetting)
                },
                clickAvatar = {
                    onIntent(HomeIntent.ClickProfile)
                },
                avatarUri = state.user?.avatarUri,
                userName = state.user?.username,
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it)
                .then(
                    if(state.isLoading || state.error != null) {
                        Modifier
                    } else {
                        Modifier.verticalScroll(rememberScrollState())
                    }
                ),
        ) {
            Spacer(Modifier.height(12.dp))
            RankingBanner()
            Spacer(Modifier.height(10.dp))
            if (state.isLoading) {
                LoadingAnimation()
            } else {
                if (state.error == null) {
                    TitleBanner(    
                        titleId = R.string.top_albums,
                        clickSeeAll = { onIntent(HomeIntent.ClickSeeAllAlbums) },
                    )
                    Spacer(Modifier.height(8.dp))
                    AlbumsSection(albums = state.albums)
                    Spacer(Modifier.height(16.dp))
                    TitleBanner(
                        titleId = R.string.top_tracks,
                        clickSeeAll = { onIntent(HomeIntent.ClickSeeAllTracks) },
                    )
                    Spacer(Modifier.height(8.dp))
                    TrackSection(tracks = state.tracks)
                    Spacer(Modifier.height(16.dp))
                    TitleBanner(
                        titleId = R.string.top_artists,
                        clickSeeAll = { onIntent(HomeIntent.ClickSeeAllArtists) },
                    )
                    Spacer(Modifier.height(8.dp))
                    ArtistBanner(
                        artists = state.artists,
                    )
                } else {
                    NoInternetSection(
                        fetchSongAgain = {
                            onIntent(HomeIntent.LoadAlbumTrackArtist)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistBanner(
    modifier: Modifier = Modifier,
    artists: List<Artist> = emptyList(),
) {
    LazyRow(
        contentPadding = PaddingValues(start = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier,
    ) {
        itemsIndexed(artists.take(5)) { index, artist ->
            ArtistItem(artist = artist)
        }
    }
}

@Composable
fun TrackSection(
    modifier: Modifier = Modifier,
    tracks: List<Track> = emptyList(),
) {
    LazyRow(
        contentPadding = PaddingValues(start = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier,
    ) {
        itemsIndexed(tracks.take(5)) { index, track ->
            TrackItem(
                track = track,
                color = identifyIndexToColor(index),
            )
        }
    }
}

@Composable
fun AlbumsSection(
    modifier: Modifier = Modifier,
    albums: List<Album> = emptyList(),
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val itemWidthDp = (screenWidthDp - 28.dp) / 2
    
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
    ) {
        albums.take(6).chunked(2).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                rowItems.forEach { item ->
                    AlbumItem(album = item, width = itemWidthDp)
                }
            }
        }
    }
}

@Composable
fun RankingBanner(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ranking),
            contentDescription = null,
            tint = Color(0xFFFFF500),
            modifier = Modifier.size(30.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.rankings),
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    BuiTuanAnhTheme {
        HomeScreen(
            state = HomeState(
                albums = listOf(
                    Album(name = "Palete", artistName = "AnhTuan", imageUrl = ""),
                    Album(name = "Palete", artistName = "AnhTuan", imageUrl = ""),
                    Album(name = "Palete", artistName = "AnhTuan", imageUrl = ""),
                    Album(name = "Palete", artistName = "AnhTuan", imageUrl = ""),
                ),
                tracks = listOf(
                    Track(
                        name = "Espresso",
                        playCount = "972846",
                        artistName = "Sabrina Carpenter",
                        imageUrl = "https://i.ibb.co/SwWxMkHc/angel.jpg",
                    ),
                    Track(
                        name = "Espresso",
                        playCount = "972846",
                        artistName = "Sabrina Carpenter",
                        imageUrl = "https://i.ibb.co/SwWxMkHc/angel.jpg",
                    ),
                    Track(
                        name = "Espresso",
                        playCount = "972846",
                        artistName = "Sabrina Carpenter",
                        imageUrl = "https://i.ibb.co/SwWxMkHc/angel.jpg",
                    ),
                    Track(
                        name = "Espresso",
                        playCount = "972846",
                        artistName = "Sabrina Carpenter",
                        imageUrl = "https://i.ibb.co/SwWxMkHc/angel.jpg",
                    ),
                    Track(
                        name = "Espresso",
                        playCount = "972846",
                        artistName = "Sabrina Carpenter",
                        imageUrl = "https://i.ibb.co/SwWxMkHc/angel.jpg",
                    ),
                ),
                artists = listOf(
                    Artist("Aerosmith", ""),
                    Artist("Aerosmith", ""),
                    Artist("Aerosmith", ""),
                    Artist("Aerosmith", ""),
                    Artist("Aerosmith", ""),
                ),
            ),
            onIntent = {},
        )
    }
}
