import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel

@Composable
fun RemoteServerStatus(activityViewModel: ActivityViewModel, activityId: String) {
    var isReachable by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(activityViewModel, activityId) {
        scope.launch {
            try {
                activityViewModel.remoteHealthCheck()
                isReachable = activityViewModel.isRemoteReachable()
            } catch (e: Exception) {
                isReachable = false
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Text("Checking connection to Remote Server...")
    } else {
        Column {
            Text(if (isReachable) "Remote Server Reachable" else "⚠️ Server is not reachable")
        }
    }
}