package com.deeraj.rssfeedapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationHelper(context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val geocoder = Geocoder(context, Locale.getDefault())

    @SuppressLint("MissingPermission") // Ensure permission is granted before calling
    fun getCityFromLocation(onResult: (city: String?) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                try {
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    val city = addresses?.firstOrNull()?.locality
                    onResult(city)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onResult(null)
                }
            } ?: run {
                onResult(null)
            }
        }
    }
}