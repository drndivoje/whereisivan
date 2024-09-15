package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ActivityCommands(
    onStart: () -> Unit,
    onStop: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(5.dp)

) {


    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        Button(
            onClick = {
                onStart()
            },
            enabled = !isRunning,
            modifier = Modifier
                .fillMaxWidth(0.3f)  // Set button width to 80% of the screen width
                .height(60.dp)  // Set the height of the button)
        )
        {

            Text("Start")
        }
        Button(onClick = { onPause() }, enabled = isRunning) {
            Text("Pause")
        }
        Button(
            onClick = {

                onStop()

            },
            enabled = isRunning
        ) {
            Text("Stop")
        }
    }
}
