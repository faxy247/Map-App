package com.example.maps.ui.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun OsmdroidMapView(
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp),
	centrePoint: GeoPoint = GeoPoint(0.0,0.0),
	zoomLevel: Double = 0.0
) {
	AndroidView(
		modifier = Modifier
			.fillMaxSize()
			.padding(contentPadding)
		,
		factory = { context ->
			val map = MapView(context)
			map.setTileSource(TileSourceFactory.MAPNIK)
			map.setMultiTouchControls(true)
			map
		},
		update = { view ->
			view.controller.setCenter(centrePoint)
			view.controller.setZoom(zoomLevel)
		}
	)
}

@Composable
fun UpdateMap(
	centrePoint: GeoPoint = GeoPoint(0.0, 0.0),
	zoomLevel: Double = 0.0,
	modifier: Modifier = Modifier
) {
	OsmdroidMapView(
		centrePoint = centrePoint,
		zoomLevel = zoomLevel,
		modifier = modifier
	)
}
