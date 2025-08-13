package com.example.buituananh.presentation.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.presentation.navigation.BottomBarItemManager.Companion.BottomBarItem
import com.example.buituananh.util.Destination

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    currentDestination: Int,
    onDestinationChange: (Int) -> Unit,
    onClick: (Destination) -> Unit
) {

    val bottomBarLists = listOf(
        BottomBarItem(
            0,
            stringResource(R.string.home),
            R.drawable.filled_home,
            R.drawable.outlined_home,
            Destination.HomeWrapper
        ),
        BottomBarItem(
            1,
            stringResource(R.string.library),
            R.drawable.filled_library,
            R.drawable.outlined_library,
            Destination.LibraryScreen
        ),
        BottomBarItem(
            2,
            stringResource(R.string.playlist),
            R.drawable.filled_playlist,
            R.drawable.outlined_playlist,
            Destination.PlaylistWrapper
        ),
    )

    NavigationBar(
        modifier = modifier,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {

        bottomBarLists.forEachIndexed { index, item ->
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
                        tint = if (currentDestination == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = {
                    Text(
                        item.title,
                        color = if (currentDestination == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }

    }

}

class BottomBarItemManager {
    companion object {
        
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
