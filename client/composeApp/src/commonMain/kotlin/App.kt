import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var hours by remember { mutableStateOf("00") }
        var minutes by remember { mutableStateOf("00") }
        var seconds by remember { mutableStateOf("00") }
        Column(
            modifier = Modifier.padding(all = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActivityCommands(modifier = Modifier.padding(all = 5.dp))
            TotalElapsedTime(hours = hours, minutes = minutes, seconds = seconds)
        }
    }
}