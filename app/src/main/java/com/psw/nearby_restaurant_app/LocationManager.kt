package com.psw.nearby_restaurant_app

class LocationManager (
    private val fusedLocationClient: FusedLocationPorviderClient,
    private val onLoctionUpdate: (LatLng) -> Unit
) {
    private var locationCallback: LocationCallback? = null

    fun startLocationUpdate() {
        try{
            val locationRequest = buildLocationRequest()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult)
            }
        }
    }
}