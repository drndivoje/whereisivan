package rocks.drnd.whereisivan.client.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import rocks.drnd.whereisivan.client.ui.theme.getLabelTextStyle
import rocks.drnd.whereisivan.client.ui.theme.getTextStyle

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun ActivityDetails(
    activity: Activity,
) {
    val labelTextStyle = getLabelTextStyle(MaterialTheme.typography)

    if (activity.startTime > 0L && activity.finishTime == 0L) {

        DefaultRow {
            TotalElapsedTime(time = activity.elapsedTimeInSeconds, isStopped = activity.isStopped)
        }
        DefaultRow {
            Column {
                Text(
                    text = "Start Time:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = convertEpochMillisToDateString(activity.startTime),
                    style = getTextStyle()
                )
            }
        }

        DefaultRow {
            Column {
                Text(
                    text = "Remote Status:",
                    style = labelTextStyle
                )
            }
            Column {

                Text(
                    text = if (activity.syncTime == 0L) "Offline" else timeStampDifferenceString(
                        activity.syncTime
                    ),
                    style = if (activity.syncTime == 0L) getTextStyle(Color.DarkGray) else getTextStyle(
                        Color.DarkGray
                    )
                )
            }
        }
    }
}

@Composable
fun DefaultRow(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(13.dp),
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}