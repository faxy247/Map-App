package com.example.maps.ui.map

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.example.maps.R
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.lang.Exception
import java.util.Locale

private const val TAG = "OsmMaps"

enum class SearchUse{ // Used to identify the use of a searched address
	LOCATE, START, END
}

class OsmMaps : Activity() {
	private lateinit var mMap : MapView
	private lateinit var mLocationOverlay: MyLocationNewOverlay
	private lateinit var mGeocoder: Geocoder
	
	var mCurrentAddress: Address? = null
	var mSearchResults : List<Address>? = null
	
	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		super.onCreate(savedInstanceState, persistentState)
		Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(this))
		Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME
	}
	
	override fun onPause() {
		super.onPause()
		val prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Configuration.getInstance().save(this, prefs);
		mMap.onPause()
	}
	
	override fun onResume() {
		super.onResume()
		Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
		mMap.onResume()
	}
	
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
				1)
		}
	}
	
	// Initialise Map View
	@Composable
	fun OsmdroidMapView(
		modifier: Modifier = Modifier,
		contentPadding: PaddingValues = PaddingValues(0.dp),
		centrePoint: GeoPoint = GeoPoint(0.0, 0.0),
		zoomLevel: Double = 15.0,
	) {
		AndroidView(
			modifier = Modifier
				.fillMaxSize()
				.padding(contentPadding),
			factory = { context ->
				mMap = MapView(context)
				mLocationOverlay = createLocationOverlay(context = context, resIcon = R.drawable.bluecircle)
				mGeocoder = Geocoder(context, Locale.getDefault())
				
				mMap.setTileSource(TileSourceFactory.MAPNIK)
				mMap.setMultiTouchControls(true)
				mMap.overlays.add(mLocationOverlay)
				
				// Only seems to be configurable here, required to load maps
				Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME
				
				mMap
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
		val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mMap)
		
		locationOverlay.setPersonIcon(locationIcon)
		locationOverlay.setPersonAnchor(0.5f,0.5f)
		locationOverlay.setDirectionIcon(locationIcon)
		locationOverlay.setDirectionAnchor(0.5f, 0.5f)
		
		locationOverlay.enableMyLocation()
		locationOverlay.enableFollowLocation()
		
		return locationOverlay
	}
	
	// Moves focus to live location
	fun toLiveLocation() {
		mLocationOverlay.enableFollowLocation()
	}
	
	// Search for location
	// Based on code from:
	// https://stackoverflow.com/questions/69148288/how-to-search-location-name-on-osmdroid-to-get-latitude-longitude
	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	fun searchLocationListByName(locationName: String) {
		try {
			val geoListener = @RequiresApi(33) object : Geocoder.GeocodeListener {
				override fun onGeocode(addresses: MutableList<Address>) {
					mSearchResults = addresses
				}
			}
			mGeocoder.getFromLocationName(locationName, R.integer.max_locations, geoListener)
		} catch (e: Exception) {
			Log.e(TAG, "Exception in searchLocationListByName: $e")
		}
	}
	
	private fun updateLocationByAddress(location: Address){
		mLocationOverlay.disableFollowLocation()
		mMap.controller.setCenter(
			GeoPoint(location.latitude, location.longitude)
		)
		mMap.controller.setZoom(19.0)
	}
	
	fun onLocationClicked(
		location: Address,
		searchUse: SearchUse
	) {
		when (searchUse) {
			SearchUse.LOCATE -> { // Go to location entered
				mLocationOverlay.disableFollowLocation()
				updateLocationByAddress(location)
				mCurrentAddress = location
			}
			SearchUse.END -> {
			
			}
			SearchUse.START -> {
			
			}
			else -> {
				Log.e(TAG, "Invalid searchUse in onLocationClicked, searchUse: $searchUse")
			}
		}
	}
}