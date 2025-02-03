package com.example.maps

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.maps.ui.navigation.MapNavHost

// Initialisation of Nav host
@Composable
fun MapApp(navController: NavHostController = rememberNavController()) {
    MapNavHost(navController = navController)
}