package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    isRunning: Boolean,
    enable: Boolean = true,
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
