package com.example.weatherapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.concurrent.TimeUnit
import androidx.compose.runtime.DisposableEffect as DisposableEffect1


@SuppressLint("MissingPermission")
@Composable
fun getUserLocation(context: Context): MutableState<LatandLong> {
    lateinit var locationCallback: LocationCallback
//The main entry point for interacting with the Fused Location Provider

    // The Fused Location Provider provides access to location APIs.
    val locationProvider: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val currentUserLocation = remember { mutableStateOf(LatandLong()) }

    LaunchedEffect(key1 = locationProvider) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                locationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            val lat = location.latitude
                            val long = location.longitude
                            currentUserLocation.value = LatandLong(latitude = lat.toString(), longitude = long.toString())
                        }
                    }.addOnFailureListener {
                        Log.e("Location_error", "${it.message}")
                    }
            }
        }
    }

    locationCallback.let {
        //An encapsulation of various parameters for requesting
        // location through FusedLocationProviderClient.
        val locationRequest: LocationRequest =
            LocationRequest.Builder(TimeUnit.SECONDS.toMillis(60)).
            setMaxUpdateDelayMillis(TimeUnit.MINUTES.toMillis(2)).
            setPriority(Priority.PRIORITY_HIGH_ACCURACY ).build()
        //use FusedLocationProviderClient to request location update
        locationProvider.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
    //4
    return currentUserLocation
}

data class LatandLong(
    val latitude: String = "",
    val longitude: String = ""
)

@SuppressLint("MissingPermission")
fun getUserLocationNonComp(context: Context, callb:(latLon:LatandLong)->Unit) {
    lateinit var locationCallback: LocationCallback
    val locationProvider: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        locationCallback = object : LocationCallback() {
            @SuppressLint("MissingPermission")
            override fun onLocationResult(result: LocationResult) {
                locationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        Log.d("addOnSuccessListener", "Location Received Success ${location}")
                        location?.let {
                            val lat = location.latitude
                            val long = location.longitude
                            callb(LatandLong(latitude = lat.toString(), longitude = long.toString()))
                            locationProvider.removeLocationUpdates(locationCallback)
                        }
                    }.addOnFailureListener {
                        Log.e("Location_error", "${it.message}")
                    }
            }
        }
    locationCallback.let {
        val locationRequest: LocationRequest =
            LocationRequest.Builder(TimeUnit.MINUTES.toMillis(60000))
                .setMaxUpdateDelayMillis(TimeUnit.MINUTES.toMillis(2000))
                .setIntervalMillis(TimeUnit.MINUTES.toMillis(5000))
                .setPriority(Priority.PRIORITY_LOW_POWER)
                .setMaxUpdates(1)
                .setGranularity(Granularity.GRANULARITY_COARSE)
                .build()
        //use FusedLocationProviderClient to request location update
        locationProvider.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}