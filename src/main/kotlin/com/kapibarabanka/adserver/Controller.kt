package com.kapibarabanka.adserver

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.SQLException
import javax.sql.DataSource

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
        val rs = stmt.executeQuery("SELECT * FROM drink_type")

        val output = mutableListOf<DrinkType>()
        while (rs.next()) {
            val id = rs.getInt("id")
            val name = rs.getString("name")
            val minAlco = rs.getFloat("min_alco")
            val maxAlco = rs.getFloat("max_alco")
            output.add(DrinkType(id, name, minAlco, maxAlco))
        }

        return output
    }

    @RequestMapping("/drinks")
    fun drinks(): MutableList<Drink> {
        val connection = dataSource.connection
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM drinks")

        val output = mutableListOf<Drink>()
        while (rs.next()) {
            val id = rs.getInt("id")
            val name = rs.getString("name")
			val type = rs.getInt("type")
            val rating = rs.getFloat("rating")
            val comment = rs.getString("comment")
            output.add(Drink(id, name, type, rating, comment))
        }

        return output
    }

    @RequestMapping("/events")
    fun events(): MutableList<Event> {
        val connection = dataSource.connection
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM events")

        val output = mutableListOf<Event>()
        while (rs.next()) {
            val id = rs.getInt("id")
            val name = rs.getString("name")
            val date = rs.getDate("date")
            val rating = rs.getFloat("rating")
            output.add(Event(id, name, date, rating))
        }

        return output
    }

    @RequestMapping("/drinksInEvents")
    fun drinksInEvents(): MutableList<DrinkInEvent> {
        val connection = dataSource.connection
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM drink_in_event")

        val output = mutableListOf<DrinkInEvent>()
        while (rs.next()) {
            val pairId = rs.getInt("pair_id")
            val eventId = rs.getInt("event_id")
            val drinkId = rs.getInt("drink_id")
            val amount = rs.getFloat("amount")
            output.add(DrinkInEvent(pairId, eventId, drinkId, amount))
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