package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBike
import androidx.compose.material.icons.rounded.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel
import java.time.Instant

@Composable
fun TrackScreen(
    activityViewModel: ActivityViewModel,
    locationClient: FusedLocationProviderClient,
) {
    val activity by activityViewModel.activityState.collectAsState()
    val isRunning by activityViewModel.isRunningState.collectAsState()

    AnimatedContent(
        targetState = isRunning,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
        modifier = Modifier.fillMaxSize(),
        label = "track_state"
    ) { running ->
        if (running) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RecordingBadge()
                ActivityDetails(activity = activity)
                Spacer(Modifier.weight(1f))
                ActivityCommands(
                    onStart = { activityViewModel.onStart(locationClient, Instant.now().toEpochMilli()) },
                    onStop = { activityViewModel.stop() },
                    isRunning = { true },
                )
                Spacer(Modifier.height(8.dp))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.DirectionsBike,
                    contentDescription = null,
                    modifier = Modifier.size(96.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Ready to Ride",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Start recording your activity to track your route and time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(48.dp))
                ActivityCommands(
                    onStart = { activityViewModel.onStart(locationClient, Instant.now().toEpochMilli()) },
                    onStop = { activityViewModel.stop() },
                    isRunning = { false },
                )
            }
        }
    }
}

@Composable
private fun RecordingBadge() {
    val infiniteTransition = rememberInfiniteTransition(label = "recording_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "dot_alpha"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.FiberManualRecord,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error.copy(alpha = alpha),
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = "Recording",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}
