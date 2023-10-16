package com.example.weatherapp.util

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionsRequired

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermiChecker(permissionsState : MultiplePermissionsState, grantedContent:@Composable() () ->Unit, notGrantedContent:@Composable()() ->Unit, notAvailableContent:@Composable()()->Unit){
    PermissionsRequired(
        multiplePermissionsState = permissionsState,
        permissionsNotGrantedContent = {
            notGrantedContent()
        },
        permissionsNotAvailableContent = {
            notAvailableContent()
        }) {
        grantedContent()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionAlertDialog(permissionsState: MultiplePermissionsState){
    val permList = permissionsState.permissions.joinToString { permissionState : PermissionState ->
        ","
    }
    AlertDialog(title = {
        Text(text = "Permission Dialog")
    },
        text = { Text(text = "The following Permissions are needed to ensure smooth operation of the app ${permList}") },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {  }) {
                Text("Grant Access")
            }
        },
        dismissButton = {
            TextButton(onClick = {  }) {
                Text("No Access")
            }
        })
}