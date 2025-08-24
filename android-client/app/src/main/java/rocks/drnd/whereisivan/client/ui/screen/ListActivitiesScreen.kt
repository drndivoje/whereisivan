package rocks.drnd.whereisivan.client.ui.screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rocks.drnd.whereisivan.client.convertEpochMillisToDateString
import rocks.drnd.whereisivan.client.viewmodel.ActivityListViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListActivitiesScreen(viewModel: ActivityListViewModel) {
    val activities by viewModel.activities.observeAsState(initial = emptyList())

    LazyColumn {
        item {
            Text(
                "Recent Activities",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        if (activities.isEmpty()) {
            item {
                Text(
                    "No activities",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        "Start Time",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Finish",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            items(count = activities.size) { index ->
                val activity = activities[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        convertEpochMillisToDateString(activity.startTime),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        if (activity.finishTime == 0L)  "Not finished" else  convertEpochMillisToDateString(activity.finishTime),
                        modifier = Modifier.weight(1f)
                    )
                }
                HorizontalDivider()
            }
        }
    }
}