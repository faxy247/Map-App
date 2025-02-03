package com.example.maps

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maps.ui.theme.MapsTheme
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import android.Manifest
import com.example.maps.ui.map.OsmdroidMapView


class MainActivity : ComponentActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
        }

        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
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
//        val ctx = applicationContext
//        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
//        Configuration.getInstance().userAgentValue = "maps"
    }
}

//@Composable
//fun OsmdroidMapView(
//    modifier: Modifier = Modifier
//) {
//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = { context ->
//            val map = MapView(context)
//            map.setTileSource(TileSourceFactory.MAPNIK)
//            map.setMultiTouchControls(true)
//            map
//        },
////        update = { view ->
////            view.controller.setCenter(GeoPoint(0.0,0.0))
////        }
//    )
//}

@Composable
fun StartOsmdroid(
    modifier: Modifier = Modifier
) {
    OsmdroidMapView()
}