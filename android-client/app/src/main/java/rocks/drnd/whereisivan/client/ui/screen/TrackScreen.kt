package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel
import java.time.Instant

@Composable
fun TrackScreen(
    activityViewModel: ActivityViewModel,
    locationClient: FusedLocationProviderClient,
    innerPadding: PaddingValues
) {
    var isRunning by remember { mutableStateOf(false) }
    val activity by activityViewModel.activityState.collectAsState()


    Column(
        modifier = Modifier
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActivityCommands(
                onStart = {
                    activityViewModel.onStart(locationClient, Instant.now().toEpochMilli());
                    isRunning = true
                },
                onStop = { activityViewModel.stop(); isRunning = false },
                onPause = {
                    activityViewModel.pause(); isRunning = false
                },
                isRunning = isRunning,
                enable = isRunning
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TotalElapsedTime(time = activity.elapsedTimeInSeconds, isStopped = activity.isStopped)
        }

        ActivityDetails(activity = activity)
    }
}