package com.example.maps.ui.map

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
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
	var addressEntered by remember { mutableStateOf("") }
	var label by remember { mutableStateOf("")}
	label = "Where are you looking for?"
	
	Scaffold(
		modifier = modifier,
		topBar = {
			TextField( // Text field to enter where to go
				modifier = modifier.fillMaxWidth(),
				value = addressEntered,
				singleLine = true,
				onValueChange = {
					addressEntered = it
					val returnLocation = maps.searchLocationListByName(addressEntered)
					if (returnLocation == null) {
						label = "Address not found"
					}
					else {
						maps.updateLocationByAddress(returnLocation)
					}
				},
				label = { Text(label) },
				keyboardOptions = KeyboardOptions.Default,
				shape = RoundedCornerShape(8.dp)
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = { maps.toLiveLocation() },
				shape = MaterialTheme.shapes.medium,
				modifier = Modifier.padding(20.dp)
			) {
				Icon(
					imageVector = Icons.Default.LocationOn,
					contentDescription = stringResource(R.string.live_location)
				)
			}
		}
	) { innerPadding ->
		maps.OsmdroidMapView(
			modifier = Modifier,
			contentPadding = innerPadding,
		)
	}
}