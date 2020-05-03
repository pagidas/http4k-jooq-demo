package me.kostasakrivos.demo.http4k

import me.kostasakrivos.demo.http4k.common.DataSourceWrapper
import me.kostasakrivos.demo.http4k.common.Database
import me.kostasakrivos.demo.http4k.config.ConfigReader

val ItemsDatabase = object: Database {
    override val dataSource = DataSourceWrapper(
        driverClassName = ConfigReader.db.driver,
        jdbcUrl = ConfigReader.jdbcUrl,
        username = ConfigReader.db.username,
        password = ConfigReader.db.password
    )
}
