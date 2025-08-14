package com.example.buituananh.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.domain.model.Album
import com.example.buituananh.presentation.components.BackTopBar

@Composable
fun TopAlbumsScreen(
    popBack: () -> Unit,
    modifier: Modifier = Modifier,
    albums: List<Album> = emptyList()
) {
    
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val itemWidthDp = screenWidthDp - 24.dp
    
    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.top_albums),
                popBack = popBack
            )
        },
        modifier = modifier
    ) {  
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) { 
          items(albums) { album ->
              AlbumItem(
                  album = album,
                  width = itemWidthDp
              )
          }  
        }
    }
    
}
