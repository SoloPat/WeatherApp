package com.example.weatherapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


data class LatandLong(
    val latitude: String = "",
    val longitude: String = ""
)

/**
 * This function returns the user location using a call back when the location is retrieved.
 * It is expected that this function will be called after permission check.
 * Todo Remove the update count check as there has been multiple call backs. This could be a recomposition issue.
 * The recomposition is fixed. Did not remove this check to avoid last minute changes.
 */
@SuppressLint("MissingPermission")
fun getUserLocation(context: Context, callb:(latLon:LatandLong)->Unit) {
    Log.d("getUserLocation", "Method Called")
    lateinit var locationCallback: LocationCallback
    val locationProvider: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        locationCallback = object : LocationCallback() {
            @SuppressLint("MissingPermission")
            override fun onLocationResult(result: LocationResult) {
                var updateCount = 0
                locationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        Log.d("addOnSuccessListener", "Location Received Success ${location}")
                        updateCount++
                        location?.let {
                            val lat = location.latitude
                            val long = location.longitude
                            if(updateCount ==1) { //Update only once.
                                callb(
                                    LatandLong(
                                        latitude = lat.toString(),
                                        longitude = long.toString()
                                    )
                                )
                            }
                        }
                        locationProvider.removeLocationUpdates(locationCallback)
                    }.addOnFailureListener {
                        Log.e("Location_error", "${it.message}")
                    }
            }
        }
    //Todo these values are set to reduce the location permission updates. The values should be finetuned
    locationCallback.let {
        val locationRequest: LocationRequest =
            LocationRequest.Builder(60000)
                .setMaxUpdateDelayMillis(60000)
                .setIntervalMillis(50000)
                .setDurationMillis(50)
                .setPriority(Priority.PRIORITY_LOW_POWER)
                .setMaxUpdates(1)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .build()
        locationProvider.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}

