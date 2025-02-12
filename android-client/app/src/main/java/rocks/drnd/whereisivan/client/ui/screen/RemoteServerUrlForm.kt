package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel

@Composable
fun RemoteServerUlrForm(activityViewModel: ActivityViewModel, onSubmit: () -> Unit) {
    var showDialog by remember { mutableStateOf(true) }
    var isValidUrl by remember { mutableStateOf(true) }
    activityViewModel.getRemoteUrl()
    AlertDialog(
        onDismissRequest = { showDialog = false },
        title = { Text("Enter Remote Server URL") },
        text = {
            Column {
                TextField(
                    value = activityViewModel.getRemoteUrl().collectAsState().value,
                    onValueChange = {
                        activityViewModel.setRemoteUrl(it)
                        isValidUrl = android.util.Patterns.WEB_URL.matcher(it).matches()
                    },
                    label = { Text("URL") },
                    isError = !isValidUrl
                )
                if (!isValidUrl) {
                    Text("Please enter a valid URL", color = Color.Red)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValidUrl) {
                        showDialog = false
                        onSubmit()
                    }
                },
                enabled = isValidUrl
            ) {
                Text("Submit")
            }


        })
}
