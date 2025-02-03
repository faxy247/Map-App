package com.example.maps.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.maps.ui.map.MapDestination
import com.example.maps.ui.map.MapScreen

// Sets up the general navigation between screens in app.
// Navigation destination items are defined at the top of each screen .kt file in an object.
@Composable
fun MapNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier
) {
	NavHost(
		navController = navController,
		startDestination = MapDestination.route,
		modifier = modifier
	) {
		composable(route = MapDestination.route) {
			MapScreen()
		}
	}
}