package com.example.maps.data

import android.content.Context
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class Routes(context : Context, userAgent : String ) {
	private val mRoadManager = OSRMRoadManager(context, userAgent)
	
	fun getRouteOverlay(pointList: ArrayList<GeoPoint>) : Polyline {
		val road = mRoadManager.getRoad(pointList)
		return RoadManager.buildRoadOverlay(road)
	}
}