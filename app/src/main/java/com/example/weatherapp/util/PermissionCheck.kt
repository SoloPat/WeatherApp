package com.example.weatherapp.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun PermissionChecker(perm : List<String>,permissionGranted:(Boolean) ->Unit){
    if(permissionsGranted(LocalContext.current, perm)){
        permissionGranted.invoke(true)
    }else{
        //Request for permission
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(), onResult ={
            grantedMap : Map<String, Boolean> ->

            permissionGranted.invoke(grantedMap.values.reduce {  grant, nextGrant -> grant && nextGrant})
        } )

        //Show permission rationale dialog and on confirm ask for permissions
        val showRationaleFor = perm.filter { ActivityCompat.shouldShowRequestPermissionRationale(LocalContext.current as Activity, it) }
        if(showRationaleFor.isEmpty()){
            launcher.launch(perm as Array<String>)
        }else{
        PermissionRationaleDialog(permissions =showRationaleFor,
            onConfirm = { launcher.launch(perm as Array<String>) })
        }
    }
}

@Composable
fun PermissionRationaleDialog(permissions : List<String>, onConfirm:()->Unit){
    AlertDialog(title = {
                        Text(text = "Permission Dialog")
    }, 
        text = { Text(text = "The following Permissions are needed to ensure smooth operation of the app ${permissions.joinToString { "," }}")}, 
        onDismissRequest = {}, 
        confirmButton = { 
            TextButton(onClick = { onConfirm() }) {
                Text("Grant Access")
            }
        },
        dismissButton = {
            TextButton(onClick = {  }) {
                Text("No Access")
            }
        })
}

fun permissionsGranted(context: Context, perm : List<String>):Boolean{
        return perm.all { ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_DENIED}
}