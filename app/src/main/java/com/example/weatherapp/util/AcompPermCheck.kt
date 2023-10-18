package com.example.weatherapp.util

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionsRequired

/**
 * This function takes a list of permissions and checks if the user has granted them. This uses
 * Accompanist third party library to make the code simpler.
 */
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

