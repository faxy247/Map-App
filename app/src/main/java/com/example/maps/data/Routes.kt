package com.example.maps.data

import android.content.Context
import android.util.Log
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import kotlin.time.TimeSource
import kotlin.time.measureTime

private const val TAG = "Routes"

class Routes(context : Context, userAgent : String ) {
	private var mOSRMRoadManager = OSRMRoadManager(context, userAgent)
	private var mSFRoadManager = SFRoadManager(context, userAgent)
	
	init {
		mOSRMRoadManager.setMean(OSRMRoadManager.MEAN_BY_FOOT)
	}
	
	fun getOSRMRouteOverlay(pointList: ArrayList<GeoPoint>) : Polyline {
		val timeSource = TimeSource.Monotonic
		val markStart = timeSource.markNow()
		val road = mOSRMRoadManager.getRoad(pointList)
		val markEnd = timeSource.markNow()
		Log.d(TAG, "OSRM Query Time: ${markEnd-markStart}")
		return RoadManager.buildRoadOverlay(road)
	}
	
	fun getSFRoute(pointList: ArrayList<GeoPoint>,
				   useDefault : Boolean = false,
				   algorithm : Int = 0
	) : Polyline {
		val timeSource = TimeSource.Monotonic
		val markStart = timeSource.markNow()
		val road = if (useDefault) {
			mSFRoadManager.getDefaultRouteTest(algorithm)
		} else {
			mSFRoadManager.getRoad(pointList)
		}
		val markEnd = timeSource.markNow()
		Log.d(TAG, "SF Query Time: ${markEnd-markStart}")
		return when (algorithm) {
			1 -> RoadManager.buildRoadOverlay(road, 0x80F87D00.toInt(), 10.0f) // colour line orange
			2 -> RoadManager.buildRoadOverlay(road, 0x80FCD63F.toInt(), 6.0f) // colour line yellow
			else -> RoadManager.buildRoadOverlay(road, 0x80E30909.toInt(), 5.0f) // colour line red
		}
		
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
			var colour = 0x80e0d8d8 // Grey for no cost
			
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
			
			lines.add(RoadManager.buildRoadOverlay(road, colour.toInt(), 10f))
		}
		
		return lines
	}
}