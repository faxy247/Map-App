package com.example.maps.data

import android.content.Context
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.bonuspack.utils.BonusPackHelper
import org.osmdroid.bonuspack.utils.PolylineEncoder

class Routes(context : Context, userAgent : String ) {
	private var mOSRMRoadManager = OSRMRoadManager(context, userAgent)
	private var mSFRoadManager = SFRoadManager(context, userAgent)
	
	init {
		mOSRMRoadManager.setMean(OSRMRoadManager.MEAN_BY_FOOT)
	}
	
	fun getOSRMRouteOverlay(pointList: ArrayList<GeoPoint>) : Polyline {
		val road = mOSRMRoadManager.getRoad(pointList)
		return RoadManager.buildRoadOverlay(road)
	}
	
	fun getSFOverlay(pointList: ArrayList<GeoPoint>) : Polyline {
		val road = mSFRoadManager.getRoad(pointList)
		return RoadManager.buildRoadOverlay(road)
	}
	
	fun getSFTest() : Polyline {
		val road = mSFRoadManager.getDefaultTest()
		return RoadManager.buildRoadOverlay(road, 0x80E30909.toInt(), 5.0f)
	}
}