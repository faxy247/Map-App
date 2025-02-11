package com.example.maps.data

import android.location.Address
import androidx.room.PrimaryKey


data class LocationItem(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val address: Address
)
