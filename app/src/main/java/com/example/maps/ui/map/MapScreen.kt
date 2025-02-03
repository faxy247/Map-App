package com.example.maps.ui.map

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
	Scaffold(
		modifier = modifier,
		topBar = {},
	) { innerPadding ->
		OsmdroidMapView(
			modifier = Modifier,
			contentPadding = innerPadding,
		)
	}
}