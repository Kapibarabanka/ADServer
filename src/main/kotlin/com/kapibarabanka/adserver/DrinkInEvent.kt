package com.kapibarabanka.adserver

data class DrinkInEvent(val id: Long, val eventId: Long, val drinkId: Long, val amount: Float,
                   val user: String, val lastUpdate: Long, val state: String)
