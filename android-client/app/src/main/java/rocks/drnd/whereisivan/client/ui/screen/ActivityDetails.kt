package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.convertEpochMillisToDateString
import rocks.drnd.whereisivan.client.timeStampDifferenceString

@Composable
internal fun ActivityDetails(activity: Activity) {
    if (activity.startTime > 0L && activity.finishTime == 0L) {
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TotalElapsedTime(time = activity.elapsedTimeInSeconds)
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            val isSynced = activity.syncTime > 0L

            ListItem(
                headlineContent = { Text("Start Time") },
                supportingContent = { Text(convertEpochMillisToDateString(activity.startTime)) },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )

            ListItem(
                headlineContent = { Text("Remote Sync") },
                supportingContent = {
                    Text(if (isSynced) timeStampDifferenceString(activity.syncTime) else "Offline")
                },
                leadingContent = {
                    Icon(
                        if (isSynced) Icons.Rounded.Cloud else Icons.Rounded.CloudOff,
                        contentDescription = null,
                        tint = if (isSynced) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
    }
}
