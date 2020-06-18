package com.kapibarabanka.adserver

import java.sql.Date

data class Event (val id: Long, val name: String, val date: Date, val rating: Float,
                  val user: String, val lastUpdate: Long, val state: String)