package com.example.maps.data

import android.content.Context
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

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
	
	fun getSFRoute(pointList: ArrayList<GeoPoint>,
				   useDefault : Boolean = false
	) : Polyline {
		val road = if (useDefault) {
			mSFRoadManager.getDefaultRouteTest()
		} else {
			mSFRoadManager.getRoad(pointList)
		}
		
		return RoadManager.buildRoadOverlay(road, 0x80E30909.toInt(), 5.0f) // colour line red
	}
	
	fun getSFHeatMap(boundingBox : Array<Double>,
					 useDefault: Boolean = false
	) : ArrayList<Polyline> {
		val roads = if (useDefault) {
			mSFRoadManager.getDefaultHeatMapTest()
		} else {
			mSFRoadManager.getHeatMap(boundingBox)
		}
		val lines = ArrayList<Polyline>()
		
		for (road in roads) {
			var colour = 0xff836E6E // Grey for no cost
			
			if (road.mLength <= 1) {
				// do nothing
			}
			else if (road.mLength < 10) {
				colour = 0x95FCD63F // Yellow - at least 1 offence
			}
			else if (road.mLength < 20) {
				colour = 0x95F87D00 // Orange - few offences
			}
			else if (road.mLength < 50) {
				colour = 0x95FC0000 // Red - many offences
			}
			else {
				colour = 0x95000000 // Black - a lot of offences
			}
			
			lines.add(RoadManager.buildRoadOverlay(road, colour.toInt(), 8f))
		}
		
		return lines
	}
}