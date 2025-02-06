package com.example.maps.ui.map

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.util.Log
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
import java.lang.Exception
import java.util.Locale

private const val TAG = "OsmMaps"

class OsmMaps : Application() {
	private lateinit var cMap : MapView
	private lateinit var cLocationOverlay: MyLocationNewOverlay
	private lateinit var cGeocoder: Geocoder
	
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
				cMap = MapView(context)
				cLocationOverlay = createLocationOverlay(context = context, resIcon = R.drawable.bluecircle)
				cGeocoder = Geocoder(context, Locale.getDefault())
				
				cMap.setTileSource(TileSourceFactory.MAPNIK)
				cMap.setMultiTouchControls(true)
				cMap.overlays.add(cLocationOverlay)
				cMap
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
		val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), cMap)
		
		locationOverlay.setPersonIcon(locationIcon)
		locationOverlay.setPersonAnchor(0.5f,0.5f)
		locationOverlay.setDirectionIcon(locationIcon)
		locationOverlay.setDirectionAnchor(0.5f, 0.5f)
		
		locationOverlay.enableMyLocation()
		locationOverlay.enableFollowLocation()
		
		return locationOverlay
	}
	
	fun toLiveLocation() {
		cLocationOverlay.enableFollowLocation()
	}
	
	// Based on code from:
	// https://stackoverflow.com/questions/69148288/how-to-search-location-name-on-osmdroid-to-get-latitude-longitude
	fun searchLocationListByName(locationName: String) : Address? { // Restricted to 1 Address for now
		try {
			val geoResults: MutableList<Address>? = cGeocoder.getFromLocationName(locationName, 1)
			if (!geoResults.isNullOrEmpty())
			{
				val address = geoResults[0]
				return address // TODO return list of addresses
			}
		} catch (e: Exception) {
			Log.e(TAG, "Exception in searchLocationListByName: $e")
		}
		return null
	}
	
	fun updateLocationByAddress(location: Address){
		cMap.controller.setCenter(
			GeoPoint(location.latitude, location.longitude)
		)
	}
}