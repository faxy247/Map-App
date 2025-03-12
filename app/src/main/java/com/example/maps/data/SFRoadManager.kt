package com.example.maps.data

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.bonuspack.utils.BonusPackHelper
import org.osmdroid.bonuspack.utils.PolylineEncoder
import org.osmdroid.util.GeoPoint
import java.net.URL
import kotlin.collections.ArrayList

private const val TAG = "SFRoadManager"

class SFRoadManager(context : Context, userAgent : String) : RoadManager() {
	var mContext = context;
	private var mUserAgent = userAgent;
	private var mBaseUrl = "http://18.169.211.77:3000/";
	
	override fun getRoad(waypoints: ArrayList<GeoPoint>?): Road {
		val roads = getRoads(waypoints)
		return roads[0]
	}
	
	override fun getRoads(waypoints: ArrayList<GeoPoint>?): Array<Road> {
		// Currently can only find a route between 2 points,
		// need to restrict waypoints to 2 elements
		if (waypoints == null || waypoints.size != 2) {
			return emptyRoad()
		}
		
		// Create Query URL and get JSON response
		val urlQuery = getUrl(waypoints[0], waypoints[1])
		val jString = BonusPackHelper.requestStringFromUrl(urlQuery, mUserAgent)
		
		if (jString.isNullOrEmpty()) {
			Log.e(TAG, "getRoads: jString request failed")
			return emptyRoad()
		}
		
		// Grab Geometry from JSON string
		try {
			val jObj = JSONObject(jString)
			if (jObj.getString("code") != "Ok"){
				// Server side error
				Log.e(TAG, "getRoads: jString returned error - " + jObj.getString("code"))
				return emptyRoad()
			}
			
			val geom = jObj.getString("geom")
			val road = Road()
			road.mRouteHigh = PolylineEncoder.decode(geom, 10, false)
			
			return arrayOf(road)
			
		} catch (err : JSONException) {
			err.message?.let { Log.e(TAG, it) }
			return emptyRoad()
		}
	}
	
	private fun emptyRoad() : Array<Road> {
		return arrayOf<Road>()
	}
	
	// Generates url needed to get the geometry
	private fun getUrl(start : GeoPoint, end: GeoPoint) : String {
		val stringUrl =
			mBaseUrl + "route?slat=${start.latitude}&slon=${start.longitude}&elat=${end.latitude}&elon=${end.longitude}"
		return stringUrl
	}
	
	// Used to test a default route
	fun getDefaultTest() : Road {
		// Create Query URL and get JSON response
		val urlQuery = mBaseUrl + "default_test"
		val jString = BonusPackHelper.requestStringFromUrl(urlQuery)
		//val jString = URL(urlQuery).readText()
		
		if (jString.isEmpty()) {
			Log.e(TAG, "getRoads: jString request failed")
			return emptyRoad()[0]
		}
		
		// Grab Geometry from JSON string
		try {
			val jObj = JSONObject(jString)
			if (jObj.getString("code") != "Ok"){
				// Server side error
				Log.e(TAG, "getRoads: jString returned error - " + jObj.getString("code"))
				return emptyRoad()[0]
			}
			
			val geom = jObj.getString("geom")
			val road = Road()
			road.mRouteHigh = PolylineEncoder.decode(geom, 10, false)
			
			return road
			
		} catch (err : JSONException) {
			err.message?.let { Log.e(TAG, it) }
			return emptyRoad()[0]
		}
	}
}