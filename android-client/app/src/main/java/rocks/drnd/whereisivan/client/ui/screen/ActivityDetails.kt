package rocks.drnd.whereisivan.client.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.convertEpochMillisToDateString
import rocks.drnd.whereisivan.client.copyTextToClipboard

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun ActivityDetails(
    activity: Activity,
) {
    val labelTextStyle = getLabelTextStyle(MaterialTheme.typography)

    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(13.dp)

    if (activity.isStarted) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TotalElapsedTime(time = activity.elapsedTimeInSeconds, isStopped = activity.isStopped)
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Copy Activity ID:",
                    style = labelTextStyle
                )
            }
            Column {
                val context = LocalContext.current
                IconButton(onClick = {
                    copyTextToClipboard(context, activity.id)
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Copy Activity ID"
                    )
                }
            }

        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Start Time:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = convertEpochMillisToDateString(activity.startTime),
                    style = getMetricTextStyle(MaterialTheme.typography)
                )
            }
        }

        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Activity Sync Time:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = convertEpochMillisToDateString(activity.syncTime),
                    style = getMetricTextStyle(MaterialTheme.typography)
                )
            }
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Waypoints Count:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = activity.locationTimestamps.size.toString(),
                    style = getMetricTextStyle(MaterialTheme.typography)
                )
            }
        }
    }
}