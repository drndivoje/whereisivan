package rocks.drnd.whereisivan.client.ui.screen

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBike
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import rocks.drnd.whereisivan.client.viewmodel.ActivityListViewModel
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel

private sealed class Screen(val route: String, val label: String) {
    data object Track : Screen("track", "Track")
    data object History : Screen("history", "History")
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun MainScreen(activityViewModel: ActivityViewModel, activityListViewModel: ActivityListViewModel) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WhereIsIvan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Screen.Track.route,
                    onClick = {
                        navController.navigate(Screen.Track.route) {
                            popUpTo(Screen.Track.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Rounded.DirectionsBike, contentDescription = Screen.Track.label) },
                    label = { Text(Screen.Track.label) }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.History.route,
                    onClick = {
                        navController.navigate(Screen.History.route) {
                            popUpTo(Screen.Track.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Rounded.History, contentDescription = Screen.History.label) },
                    label = { Text(Screen.History.label) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Track.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(220)) },
            exitTransition = { fadeOut(animationSpec = tween(220)) },
        ) {
            composable(Screen.Track.route) {
                TrackScreen(activityViewModel)
            }
            composable(Screen.History.route) {
                ListActivitiesScreen(activityListViewModel)
            }
        }
    }
}
