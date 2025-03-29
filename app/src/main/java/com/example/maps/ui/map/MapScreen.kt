package com.example.maps.ui.map

import android.location.Address
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.maps.R
import com.example.maps.ui.navigation.AppNavDestination
import org.osmdroid.util.GeoPoint
import java.util.Locale


object MapDestination : AppNavDestination {
	override val route = "Map"
	override val showBanner = false
	override val titleRes = null
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MapScreen(
	modifier: Modifier = Modifier
) {
	val maps = OsmMaps()
	var addressEntered by remember { mutableStateOf("") }
	var addressDetails by remember { mutableStateOf("")}
	var addressList : MutableList<Address>? = null
	
	Surface {
		maps.OsmdroidMapView(
			modifier = Modifier,
		)
		
		// Top Search Bar - not contained in own function due to addressDetails updating
		Column(
			modifier = Modifier.statusBarsPadding()
		) {
			TextField( // Text field to enter where to go
				modifier = modifier.fillMaxWidth(),
				value = addressEntered,
				singleLine = true,
				onValueChange = {
					addressEntered = it
					addressList = maps.searchLocationListByName(addressEntered)
				},
				label = { Text("Where are you looking for?") },
				keyboardOptions = KeyboardOptions.Default,
				shape = RoundedCornerShape(12.dp),
			)
			if (addressEntered.isNotBlank()) {
				SearchResults(
					Modifier,
					addressList,
					onLocationClicked = {
						maps.onLocationClicked(it, SearchUse.LOCATE)
						addressEntered = ""
						addressDetails = maps.getCurrentAddressDetails()
					}
				)
			}
		}
		
		MapsBottomBar(modifier, maps, addressDetails)
	}
	
}

@Composable
private fun MapsBottomBar(
	modifier: Modifier = Modifier,
	maps: OsmMaps,
	addressDetails : String
) {
	Column(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalAlignment = Alignment.End
	) {
		Spacer(Modifier.weight(2f))
		// Live Location Button
		FilledIconButton(
			onClick = { maps.toLiveLocation() },
			shape = MaterialTheme.shapes.medium,
			modifier = Modifier
				.padding(top = 5.dp, bottom = 5.dp, end = 20.dp),
		) {
			Icon(
				imageVector = Icons.Default.LocationOn,
				contentDescription = stringResource(R.string.live_location)
			)
		}
		// Test Route Button
		FilledIconButton(
			onClick = {
				val pointList = ArrayList<GeoPoint>()
				pointList.add(maps.getLiveLocation())
				maps.showRoute(pointList, defaultTest = true) },
			shape = MaterialTheme.shapes.medium,
			modifier = Modifier
				.padding(top = 5.dp, bottom = 5.dp, end = 20.dp),
		) {
			Icon(
				imageVector = Icons.Default.Favorite,
				contentDescription = stringResource(R.string.live_location)
			)
		}
		
		// Test Heat Map Button
		FilledIconButton(
			onClick = {
				maps.showHeatMap(defaultTest = true)
			},
			shape = MaterialTheme.shapes.medium,
			modifier = Modifier
				.padding(top = 5.dp, bottom = 20.dp, end = 20.dp)
		) {
			Icon(
				imageVector = Icons.Filled.Star,
				contentDescription = stringResource(R.string.live_location)
			)
		}
		
		Card(
			modifier
				.background(MaterialTheme.colorScheme.background)
				.navigationBarsPadding()
				.fillMaxWidth()
		) {
			Column {
				// Address Result Bar
				if (addressDetails.isNotBlank()) {
					Text(addressDetails)
					
					Row {
						Button(
							modifier = Modifier
								.weight(1f),
							onClick = {
								val pointList = ArrayList<GeoPoint>()
								pointList.add(maps.getLiveLocation())
								pointList.add(maps.getCurrentAddress())
								maps.showRoute(pointList, useSF = false)
							}
						) {
							Text("OSRM Directions")
						}
						Button(
							modifier = Modifier
								.weight(1f),
							onClick = {
								val pointList = ArrayList<GeoPoint>()
								pointList.add(maps.getLiveLocation())
								pointList.add(maps.getCurrentAddress())
								maps.showRoute(pointList)
							}
						) {
							Text("SF Directions")
						}
						Button(
							modifier = Modifier
								.weight(1f),
							onClick = {
								maps.showHeatMap()
							}
						) {
							Text("Crime Map")
						}
					}
				}
			}
		}
	}
}

@Composable
private fun SearchResults(
	modifier: Modifier = Modifier,
	addressList: List<Address>?,
	onLocationClicked: (Address) -> Unit
) {
	Column (
		modifier = modifier,
	) {
			SearchList(
				modifier,
				addressList,
				onLocationClicked
			)
	}
}

@Composable
private fun SearchList( // Needed as LazyColumn causes problems with calling Composable functions
	modifier: Modifier = Modifier,
	addressList: List<Address>?,
	onLocationClicked: (Address) -> Unit
) {
	if (addressList.isNullOrEmpty()){
		Card(
			modifier = modifier
				.fillMaxWidth()
				.background(MaterialTheme.colorScheme.background)
		){Text( text = "No Locations Found" )}
	} else {
		LazyColumn {
			items(
				items = addressList,
				key = { it.latitude } // TODO: find alternative to using latitude
			) { location ->
				SearchedLocation(
					modifier = Modifier
						.clickable {
							onLocationClicked(location)
						},
					location = location
				)
			}
		}
	}
}

@Composable
private fun SearchedLocation(
	modifier : Modifier = Modifier,
	location : Address
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.background)
			.padding(8.dp)
	) {
		Column {
			Text (
				text = location.getAddressLine(0)
			)
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun BottomBar() {
	val mapTest = OsmMaps()
	val address = Address(Locale.getDefault())
	mapTest.updateLocationByAddress(address)
	MapsBottomBar(Modifier, mapTest, "Test")
}