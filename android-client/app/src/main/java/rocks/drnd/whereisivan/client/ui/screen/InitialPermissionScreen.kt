package rocks.drnd.whereisivan.client.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import rocks.drnd.whereisivan.client.base.PermissionBox
import rocks.drnd.whereisivan.client.viewmodel.ActivityListViewModel
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel

@SuppressLint("MissingPermission")
@Composable
fun InitialPermissionScreen(
    activityViewModel: ActivityViewModel,
    activityListViewModel: ActivityListViewModel
) {
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first()),
        onGranted = {
            MainScreen(
                activityViewModel,
                activityListViewModel
            )
        },
    )
}

