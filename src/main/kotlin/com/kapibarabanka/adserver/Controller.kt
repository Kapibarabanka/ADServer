package com.kapibarabanka.adserver

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.SQLException
import javax.sql.DataSource

const val ADMIN_USER = "admin"

const val COL_ID = "id"
const val COL_USER = "user"
const val COL_TIME = "last_update"
const val COL_STATE = "state"

const val COL_NAME = "name"
const val COL_RATING = "rating"

const val TYPES_TABLE = "drink_types"
const val COL_MIN_ALCO = "min_alco"
const val COL_MAX_ALCO = "max_alco"
const val COL_ICON = "icon"

const val DRINKS_TABLE = "drinks"
const val COL_TYPE = "type_id"
const val COL_COMMENT = "comment"

const val EVENTS_TABLE = "events"
const val COL_DATE = "date"

const val DRINKS_IN_EVENTS_TABLE = "drinks_in_events"
const val COL_EVENT = "event_id"
const val COL_DRINK = "drink_id"
const val COL_AMOUNT = "amount"

@RestController
class Controller {

    @Value("\${spring.datasource.url}")
    private var dbUrl: String? = null

    @Autowired
    private lateinit var dataSource: DataSource

    @RequestMapping("/")
    internal fun index(): String {
        return "index"
    }

    @RequestMapping("/drinkTypes")
    fun drinkTypes(): MutableList<DrinkType> {
        val connection = dataSource.connection
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM $TYPES_TABLE")

        val output = mutableListOf<DrinkType>()
        while (rs.next()) {
            val id = rs.getLong(COL_ID)
            val name = rs.getString(COL_NAME)
            val minAlco = rs.getFloat(COL_MIN_ALCO)
            val maxAlco = rs.getFloat(COL_MAX_ALCO)
            val icon = rs.getInt(COL_ICON)
            val user = rs.getString(COL_USER)
            val lastUpdate = rs.getLong(COL_TIME)
            val state = rs.getString(COL_STATE)
            output.add(DrinkType(id, name, minAlco, maxAlco, icon, user, lastUpdate, state))
        }

        return output
    }

    @RequestMapping("/drinks")
    fun drinks(): MutableList<Drink> {
        val connection = dataSource.connection
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM $DRINKS_TABLE")

        val output = mutableListOf<Drink>()
        while (rs.next()) {
            val id = rs.getLong(COL_ID)
            val name = rs.getString(COL_NAME)
			val typeId = rs.getLong(COL_TYPE)
            val rating = rs.getFloat(COL_RATING)
            val comment = rs.getString(COL_COMMENT)
            val user = rs.getString(COL_USER)
            val lastUpdate = rs.getLong(COL_TIME)
            val state = rs.getString(COL_STATE)
            output.add(Drink(id, name, typeId, rating, comment, user, lastUpdate, state))
        }

        return output
    }

    @RequestMapping("/events")
    fun events(): MutableList<Event> {
        val connection = dataSource.connection
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM $EVENTS_TABLE")

        val output = mutableListOf<Event>()
        while (rs.next()) {
            val id = rs.getLong(COL_ID)
            val name = rs.getString(COL_NAME)
            val date = rs.getDate(COL_DATE)
            val rating = rs.getFloat(COL_RATING)
            val user = rs.getString(COL_USER)
            val lastUpdate = rs.getLong(COL_TIME)
            val state = rs.getString(COL_STATE)
            output.add(Event(id, name, date, rating, user, lastUpdate, state))
        }

        return output
    }

    @RequestMapping("/drinksInEvents")
    fun drinksInEvents(): MutableList<DrinkInEvent> {
        val connection = dataSource.connection
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM $DRINKS_IN_EVENTS_TABLE")

        val output = mutableListOf<DrinkInEvent>()
        while (rs.next()) {
            val id = rs.getLong(COL_ID)
            val eventId = rs.getLong(COL_EVENT)
            val drinkId = rs.getLong(COL_DRINK)
            val amount = rs.getFloat(COL_AMOUNT)
            val user = rs.getString(COL_USER)
            val lastUpdate = rs.getLong(COL_TIME)
            val state = rs.getString(COL_STATE)
            output.add(DrinkInEvent(id, eventId, drinkId, amount, user, lastUpdate, state))
        }

        return output
    }

    @Bean
    @Throws(SQLException::class)
    fun dataSource(): DataSource {
        return if (dbUrl?.isEmpty() ?: true) {
            HikariDataSource()
        } else {
            val config = HikariConfig()
            config.jdbcUrl = dbUrl
            HikariDataSource(config)
        }
    }
}