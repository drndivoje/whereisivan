package rocks.drnd.whereisivan.client.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.clickable
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun MainScreen(activityViewModel: ActivityViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Location Tracker") },
            )
        },
        bottomBar = {
            BottomAppBar {
                val isMainScreen = navController.currentBackStackEntry?.destination?.route == "main"

                Text(
                    "Track",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate("main") },
                    color = if (isMainScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

                )
                Text("Archive", modifier = Modifier.weight(1f))
            }
        }
    ) { innerPadding ->
        innerPadding.calculateTopPadding()

        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                TrackScreen(activityViewModel, locationClient, innerPadding)
            }
        }
    }
}