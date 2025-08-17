package rocks.drnd.whereisivan.client.ui.screen

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import rocks.drnd.whereisivan.client.formatTime
import rocks.drnd.whereisivan.client.ui.theme.getLabelTextStyle

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun TotalElapsedTime(
    time: Long,
    labelTextStyle: TextStyle = getLabelTextStyle(MaterialTheme.typography),
    isStopped: Boolean


) {
    val timeString = if (isStopped) 0L.formatTime() else time.formatTime()

    Text(
        text = "Total Elapsed Time:",
        style = labelTextStyle
    )
    Text(
        text = timeString,
        style = TextStyle(
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold,
        )
    )
}