package rocks.drnd.whereisivan.client.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.koin.android.ext.android.inject
import rocks.drnd.whereisivan.client.ui.theme.Client3Theme
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityViewModel: ActivityViewModel by inject()

        setContent {
            Client3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrentLocationScreen(activityViewModel)
                }
            }
        }
    }
}
