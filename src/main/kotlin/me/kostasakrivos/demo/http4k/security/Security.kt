package me.kostasakrivos.demo.http4k.security

import me.kostasakrivos.demo.http4k.config.ConfigReader
import org.http4k.contract.security.BasicAuthSecurity

object Security {
    val basicAuth = BasicAuthSecurity(ConfigReader.auth.realm, ConfigReader.auth.credentials)
}