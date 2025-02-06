package com.example.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maps.ui.theme.MapsTheme


class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
		}

		super.onCreate(savedInstanceState)

		enableEdgeToEdge()
		setContent {
			MapsTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					MapApp()
				}
			}
		}
	}
}

// Code below used to test functionality of base osmDroid code.
/*
class MainActivity : ComponentActivity() {
	private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
	private lateinit var map : MapView
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
		setContentView(R.layout.map)
		
		map = findViewById<MapView>(R.id.map)
		map.setTileSource(TileSourceFactory.MAPNIK)
		
		val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), map)
		
	}
	
	override fun onResume() {
		super.onResume()
		map.onResume() //needed for compass, my location overlays, v6.0.0 and up
	}
	
	override fun onPause() {
		super.onPause()
	}
	
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray,
		deviceId: Int
	) {
		val permissionsToRequest = ArrayList<String>()
		var i = 0
		while (i < grantResults.size) {
			permissionsToRequest.add(permissions[i])
			i++
		}
		if (permissionsToRequest.size > 0) {
			ActivityCompat.requestPermissions(
				this,
				permissionsToRequest.toTypedArray(),
				REQUEST_PERMISSIONS_REQUEST_CODE)
		}
	}
}
*/