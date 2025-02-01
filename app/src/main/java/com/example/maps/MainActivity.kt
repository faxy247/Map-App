package com.example.maps

import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maps.ui.theme.MapsTheme

import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import android.Manifest
import org.osmdroid.config.Configuration


class MainActivity : ComponentActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET),0)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    OsmdroidMapView()
                }
            }
        }

        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = "Maps"

        //osm maps
//        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
//
//        setContentView(R.layout.main)
//
//        map = findViewById<MapView>(R.id.map)
//        map.setTileSource(TileSourceFactory.MAPNIK)
//
//        val mapController = map.controller
//        mapController.setZoom(9.5)
//        val startPoint = GeoPoint(53.8223,-1.6315)
//        mapController.setCenter(startPoint);

    }

    override fun onResume() {
        super.onResume()
        // refresh osmdroid config on resuming.
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        // refresh osmdroid config on resuming.
        map.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        // osmdroid config
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
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }
}

@Composable
fun OsmdroidMapView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = MapView(context)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView
        }
    )
}
