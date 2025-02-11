package com.example.maps.ui.map

import android.location.Address
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.maps.R
import com.example.maps.ui.navigation.AppNavDestination


object MapDestination : AppNavDestination {
	override val route = "Map"
	override val showBanner = false
	override val titleRes = null
}

@Composable
fun MapScreen(
	modifier: Modifier = Modifier
) {
	val maps = OsmMaps()
	
	Box( // Used instead of scaffold as scaffold causes map to reload
		Modifier
	) {
		maps.OsmdroidMapView(
			modifier = Modifier,
		)
		Surface {
			/*FloatingActionButton(
				onClick = { maps.toLiveLocation() },
				shape = MaterialTheme.shapes.medium,
				modifier = Modifier.padding(20.dp)
			) {
				Icon(
					imageVector = Icons.Default.LocationOn,
					contentDescription = stringResource(R.string.live_location)
				)
			}*/
			MapsTopBar(modifier, maps)
		}
	}
}

@Composable
private fun MapsTopBar(
	modifier: Modifier = Modifier,
	maps: OsmMaps,
) {
	var addressEntered by remember { mutableStateOf("") }
	var label by remember { mutableStateOf("")}
	label = "Where are you looking for?"
	
	Column(
		modifier = Modifier.statusBarsPadding()
	) {
		TextField( // Text field to enter where to go
			modifier = modifier.fillMaxWidth(),
			value = addressEntered,
			singleLine = true,
			onValueChange = {
				addressEntered = it
				maps.searchLocationListByName(addressEntered)
			},
			label = { Text(label) },
			keyboardOptions = KeyboardOptions.Default,
			shape = RoundedCornerShape(12.dp),
		)
		if (addressEntered.isNotBlank()) {
			SearchResults(
				Modifier,
				maps,
				onLocationClicked = { maps.onLocationClicked(it, SearchUse.LOCATE) }
			)
		}
	}
}

@Composable
private fun MapsBottomBar(
	modifier: Modifier = Modifier,
	maps: OsmMaps
) {
	if (maps.mCurrentAddress != null) {
		Card(
			modifier
				.background(MaterialTheme.colorScheme.background)
				.navigationBarsPadding()
				.fillMaxWidth()
		) {
			Column {
				Text(text = maps.mCurrentAddress!!.getAddressLine(0))
				Text(text = "Coordinates: ${maps.mCurrentAddress!!.latitude}, ${maps.mCurrentAddress!!.longitude}")
			}
		}
	}
}

@Composable
private fun SearchResults(
	modifier: Modifier = Modifier,
	maps: OsmMaps,
	onLocationClicked: (Address) -> Unit
) {
	Column (
		modifier = modifier,
	) {
			SearchList(
				modifier,
				maps,
				onLocationClicked
			)
	}
}

@Composable
private fun SearchList( // Needed as LazyColumn causes problems with calling Composable functions
	modifier: Modifier = Modifier,
	maps: OsmMaps,
	onLocationClicked: (Address) -> Unit
) {
	if (maps.mSearchResults.isNullOrEmpty()){
		Card(
			modifier = modifier
				.fillMaxWidth()
				.background(MaterialTheme.colorScheme.background)
		){Text( text = "No Locations Found" )}
	} else {
		LazyColumn {
			items(
				items = maps.mSearchResults!!,
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

