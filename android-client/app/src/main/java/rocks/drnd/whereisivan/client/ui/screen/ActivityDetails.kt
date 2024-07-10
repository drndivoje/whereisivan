package rocks.drnd.whereisivan.client.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import rocks.drnd.whereisivan.client.Activity

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun ActivityDetails(
    activity: Activity,
    labelTextStyle: TextStyle = TextStyle(
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = FontWeight.Bold,
        color = Color.Blue
    )


) {
    Row {
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
    Row {
        Column {
            Text(
                text = "Count sent tracks:",
                style = labelTextStyle
            )
        }
        Column {
            Text(
                text = activity.countSentLocationTracks.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                )
            )
        }

    }
}