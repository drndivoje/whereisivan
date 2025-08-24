/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

