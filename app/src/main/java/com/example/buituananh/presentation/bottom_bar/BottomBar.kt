package com.example.buituananh.presentation.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.buituananh.model.Song
import com.example.buituananh.presentation.bottom_bar.BottomBarItemManager.Companion.bottomBarLists
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
                        imageVector = if (currentDestination == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = null
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
            BottomBarItem(0, "Home", Icons.Filled.Home, Icons.Outlined.Home, Destination.HomeScreen),
            BottomBarItem(
                1,
                "Library",
                Icons.Filled.LocalLibrary,
                Icons.Outlined.LocalLibrary,
                Destination.LibraryScreen
            ),
            BottomBarItem(
                2,
                "Home",
                Icons.Filled.PlaylistPlay,
                Icons.Outlined.PlaylistPlay,
                Destination.PlaylistScreen
            ),
        )

        data class BottomBarItem(
            val id: Int,
            val title: String,
            val selectedIcon: ImageVector,
            val unselectedIcon: ImageVector,
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