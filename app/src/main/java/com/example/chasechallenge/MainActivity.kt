package com.example.chasechallenge

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.example.chasechallenge.ui.screen.WeatherApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }

        sharedPreferences = getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Quick hack: Would have done this in compose and moved to a separate file
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Location access granted. Throwing toast since it wont refresh on initial app creation
                Toast.makeText(this, "Current location will be loaded next app load", Toast.LENGTH_LONG).show()
                fetchLocation()
            }else {
                // Permission not granted
                sharedPreferences.edit().putBoolean("PermissionDenied", true).apply()
            }
        }

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        // Checks if location was granted and if previously asked so does not spam each app load
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && !sharedPreferences.getBoolean("PermissionDenied", false)
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            // Permission already granted
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    sharedPreferences.edit {
                        putString("lat", latitude.toString())
                        putString("lon", longitude.toString())
                    }
                    locationManager.removeUpdates(this)
                }
            }

            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
        }
    }
}