package rocks.drnd.whereisivan.client.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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

    val scale by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    if (activity.isStarted) {
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
                    text = "Activity is ${if (activity.syncTime > 0) "" else "not"} synced with remote server",
                    style = labelTextStyle
                )
            }
        }
        Row {
            Column {
                Text(
                    text = "Number of sync remote errors:",
                    style = labelTextStyle
                )
            }
            Column {
                Text(
                    text = "0",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

        }
        if (activity.syncTime > 0) {
            Row {
                Column {
                    Text(
                        text = "Last Sync time with remote server:",
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

        }

    } else {
        Row {
            Column {
                Text(
                    text = "Press Start Button to start new activity",
                    style = labelTextStyle,

                    modifier = Modifier.scale(scale)
                )
            }
        }
    }


}