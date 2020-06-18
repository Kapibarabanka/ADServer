package com.kapibarabanka.adserver

data class Drink (val id: Long, val name: String, val typeId: Long, val rating: Float, val comment: String,
                  val user: String, val lastUpdate: Long, val state: String)