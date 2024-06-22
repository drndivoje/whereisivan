package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(5.dp)

) {

    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        Button(onClick = {
            onStart()

            /*val response = runBlocking {
                activityWebService.startActivity()
            }
            if (response.status == HttpStatusCode.OK) {

            }*/


        }) {

            Text("Start")
        }
        Button(onClick = { onPause() }) {
            Text("Pause")
        }
        Button(onClick = {

            onStop()

        }) {
            Text("Stop")
        }
    }

}