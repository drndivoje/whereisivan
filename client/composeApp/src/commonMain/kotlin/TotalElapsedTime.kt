import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun TotalElapsedTime(
    hours: String,
    minutes: String,
    seconds: String,
    labelTextStyle: TextStyle =  TextStyle(
        fontSize = MaterialTheme.typography.subtitle1.fontSize,
        fontWeight = FontWeight.Bold,
        color = Color.Blue
    )


    ) {
    Row {
        Text(
            text = "Total Elapsed Time:",
            style = labelTextStyle
        )
    }

    Row(
        modifier = Modifier.padding(all = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        AnimatedContent(targetState = hours) {
            Text(
                text = hours,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.h1.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = if (hours == "00") Color.Gray else Color.Blue
                )
            )
        }
        AnimatedContent(targetState = minutes) {
            Text(
                text = minutes, style = TextStyle(
                    fontSize = MaterialTheme.typography.h1.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = if (minutes == "00") Color.Gray else Color.Blue
                )
            )
        }
        AnimatedContent(targetState = seconds) {
            Text(
                text = seconds, style = TextStyle(
                    fontSize = MaterialTheme.typography.h1.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = if (seconds == "00") Color.Gray else Color.Blue
                )
            )
        }
    }
}