package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val activity by activityViewModel.activityState.collectAsState()
    val isRunning by activityViewModel.isRunningState.collectAsState()

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isRunning) {
                Text(
                    text = "Press Start Activity button to start activity",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(13.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            ActivityCommands(
                onStart = {
                    activityViewModel.onStart(locationClient, Instant.now().toEpochMilli());
                },
                onStop = { activityViewModel.stop() },
                isRunning = { isRunning },
            )
        }

        ActivityDetails(activity = activity)
    }
}