import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ActivityCommands(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(5.dp)

) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        Button(onClick = { }) {
            Text("Start")
        }
        Button(onClick = { }) {
            Text("Pause")
        }
        Button(onClick = { }) {
            Text("Stop")
        }
    }

}