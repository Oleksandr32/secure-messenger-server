package database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object Database {

    @JvmStatic
    fun init() {
        /*val config = HikariConfig("/hikari.properties")
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)*/
    }
}