package com.example.maps.ui.navigation

/*
Used to navigate through each app screen.
This has been based off of the NavHost example given in Android Tutorials for building with Compose.
Explicitly, this is from their Inventory app, link to equivalent file is below:
https://github.com/google-developer-training/basic-android-kotlin-compose-training-inventory-app/blob/main/app/src/main/java/com/example/inventory/ui/navigation/NavigationDestination.kt
*/

interface AppNavDestination {
	val route: String           // Name of Screen used
	val showBanner: Boolean     // Banner visible
	val titleRes: Int?			// Id to Banner string, set to null if banner not used
}