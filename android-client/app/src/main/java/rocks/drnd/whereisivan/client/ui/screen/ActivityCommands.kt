package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal fun ActivityCommands(
    onStart: () -> Unit,
    onStop: () -> Unit,
    isRunning: Boolean,
) {


    Button(
        onClick = {
            onStart()
        },

        enabled = !isRunning,
    )
    {

        Text("Start Activity")
    }
    Button(
        onClick = {
            onStop()
        },
        enabled = isRunning,
    ) {
        Text("Stop Activity")
    }
}
