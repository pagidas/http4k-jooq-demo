package me.kostasakrivos.demo.http4k.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.kostasakrivos.demo.http4k.config.ConfigReader
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import javax.sql.DataSource

object Database {
    operator fun invoke(dataSource: DataSource, sqlDialect: SQLDialect): DSLContext {
        return DSL.using(dataSource, sqlDialect)
    }
}

class DataSourceWrapper(
    driverClassName: String? = null,
    jdbcUrl: String? = null,
    maximumPoolSize: Int? = null,
    isAutoCommit: Boolean? = true,
    user: String? = null) : HikariDataSource(

    HikariConfig().apply {
        this.driverClassName = driverClassName
        this.jdbcUrl = jdbcUrl
        this.maximumPoolSize = maximumPoolSize!!
        this.isAutoCommit = isAutoCommit!!
        this.username = user
        validate()
    })