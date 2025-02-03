package com.example.maps.ui.map

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.maps.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class OsmMaps : Application() {
	private lateinit var map : MapView
	
	@Composable
	fun OsmdroidMapView(
		modifier: Modifier = Modifier,
		contentPadding: PaddingValues = PaddingValues(0.dp),
		centrePoint: GeoPoint = GeoPoint(0.0, 0.0),
		zoomLevel: Double = 8.0,
	) {
		AndroidView(
			modifier = Modifier
				.fillMaxSize()
				.padding(contentPadding),
			factory = { context ->
				map = MapView(context)
				val locationOverlay = createLocationOverlay(context = context, resIcon = R.drawable.bluecircle)
				
				map.setTileSource(TileSourceFactory.MAPNIK)
				map.setMultiTouchControls(true)
				map.overlays.add(locationOverlay)
				map
			},
			update = { view ->
				view.controller.setCenter(centrePoint)
				view.controller.setZoom(zoomLevel)
			}
		)
	}
	
	// Creates live location overlay
	private fun createLocationOverlay(context: Context, resIcon: Int) : MyLocationNewOverlay {
		val locationIcon = Bitmap.createScaledBitmap(
			BitmapFactory.decodeResource(context.resources, resIcon),
			50,
			50,
			true
		) // Rescale icon to fit on screen properly
		val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), map)
		
		locationOverlay.setPersonIcon(locationIcon)
		locationOverlay.setPersonAnchor(0.5f,0.5f)
		locationOverlay.setDirectionIcon(locationIcon)
		locationOverlay.setDirectionAnchor(0.5f, 0.5f)
		
		locationOverlay.enableMyLocation()
		locationOverlay.enableFollowLocation()
		
		return locationOverlay
	}
}