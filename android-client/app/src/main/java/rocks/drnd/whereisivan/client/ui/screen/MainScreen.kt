package rocks.drnd.whereisivan.client.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var showDialog by remember { mutableStateOf(false) }
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Location Tracker") },
            )
        }
    ) { innerPadding ->
        innerPadding.calculateTopPadding()

        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
              RemoteServerUlrForm(
                    activityViewModel,
                    onSubmit = { navController.navigate("track") })
            }
            composable("track") {
                TrackScreen(activityViewModel, locationClient, innerPadding)
            }
        }
    }
}