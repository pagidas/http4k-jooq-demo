package me.kostasakrivos.demo.http4k

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.http4k.format.AutoMappingConfiguration
import org.http4k.format.ConfigurableJackson
import org.http4k.format.asConfigurable
import org.http4k.format.withStandardMappings
import org.http4k.lens.BiDiMapping

object ItemJson: ConfigurableJackson(KotlinModule()
    .asConfigurable()
    .withStandardMappings()
    .withItemMappings()
    .done()
    .deactivateDefaultTyping()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
)

fun AutoMappingConfiguration<ObjectMapper>.withItemMappings() = apply {
    text(BiDiMapping(::ItemName, ItemName::value))
    int(BiDiMapping(::ItemId, ItemId::value))
}
