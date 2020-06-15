package com.kapibarabanka.adserver

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.postgresql.jdbc2.optional.SimpleDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
//import org.springframework.stereotype.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.SQLException
import java.util.*
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

    @RequestMapping("/db")
    internal fun db(model: MutableMap<String, Any>): String {
        val connection = dataSource.connection
        try {
            val stmt = connection.createStatement()
            val rs = stmt.executeQuery("SELECT * FROM drink_type")

            val output = ArrayList<String>()
            while (rs.next()) {
                output.add("Read from DB: " + rs.getString("name"))
            }

            model["records"] = output
            return "db"
        } catch (e: Exception) {
            connection.close()
            model["message"] = e.message ?: "Unknown error"
            return "error"
        }

    }

    @RequestMapping("/drinkTypes")
    public fun drinkTypes(): MutableList<DrinkType> {
        val connection = dataSource.connection
            val stmt = connection.createStatement()
            val rs = stmt.executeQuery("SELECT * FROM drink_type")

            val output = mutableListOf<DrinkType>()
            while (rs.next()) {
                val name = rs.getString("name")
                val minAlco = rs.getFloat("min_alco")
                val maxAlco = rs.getFloat("max_alco")
                output.add(DrinkType(name, minAlco, maxAlco))
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