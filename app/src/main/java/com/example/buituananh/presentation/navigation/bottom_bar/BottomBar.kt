package com.example.buituananh.presentation.navigation.bottom_bar

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.presentation.navigation.bottom_bar.BottomBarItemManager.Companion.bottomBarLists
import com.example.buituananh.util.Destination

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    currentDestination: Int,
    onDestinationChange: (Int) -> Unit,
    onClick: (Destination) -> Unit
) {

    val barItemLists = remember {
        mutableStateOf(bottomBarLists)
    }

    NavigationBar(
        modifier = modifier,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {

        barItemLists.value.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination == index,
                onClick = {
                    onClick(item.destination)
                    onDestinationChange(index)
                },
                icon = {
                    Icon(
                        painter = painterResource(if (currentDestination == index) item.selectedIcon else item.unselectedIcon),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = {
                    Text(item.title)
                }
            )
        }

    }

}

class BottomBarItemManager {
    companion object {
        val bottomBarLists = listOf(
            BottomBarItem(
                0,
                "Home",
                R.drawable.filled_home,
                R.drawable.outlined_home,
                Destination.HomeScreen
            ),
            BottomBarItem(
                1,
                "Library",
                R.drawable.filled_library,
                R.drawable.outlined_library,
                Destination.LibraryScreen
            ),
            BottomBarItem(
                2,
                "Playlist",
                R.drawable.filled_playlist,
                R.drawable.outlined_playlist,
                Destination.PlaylistScreen
            ),
        )

        data class BottomBarItem(
            val id: Int,
            val title: String,
            val selectedIcon: Int,
            val unselectedIcon: Int,
            val destination: Destination
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewBottomBar(modifier: Modifier = Modifier) {

//    BottomBar {
//
//    }

}