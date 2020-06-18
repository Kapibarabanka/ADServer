package com.kapibarabanka.adserver

data class DrinkType(val id: Long, val name: String, val minAlco: Float, val maxAlco: Float, val icon: Int,
                     val user: String, val lastUpdate: Long, val state: String)