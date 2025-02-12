package rocks.drnd.whereisivan.client.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import rocks.drnd.whereisivan.client.Activity

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun ActivityDetails(
    activity: Activity,
    ) {
    val labelTextStyle = TextStyle(
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = FontWeight.Bold,
        color = if (activity.syncTime == 0L) Color.Gray else Color.Green
    )

    if (activity.isStarted) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Activity Id:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = activity.id,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                    )
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
            Column {
                Text(
                    text = "Activity Start Time:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = activity.startTime.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                    )
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
            Column {
                Text(
                    text = "Activity Sync Time:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = activity.syncTime.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                    )
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
            Column {
                Text(
                    text = "Waypoints Count:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = activity.locationTimestamps.size.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }
    }
}