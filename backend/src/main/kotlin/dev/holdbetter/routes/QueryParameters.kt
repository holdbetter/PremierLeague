package dev.holdbetter.routes

import dev.holdbetter.network.ApiFootballConfig

interface Parameter {
    val value: String
    val name: String
}

@JvmInline
value class Category(override val value: String = ApiFootballConfig.SPORT_CATEGORY) : Parameter {
    override val name: String
        get() = "Category"
}

@JvmInline
value class Country(
    override val value: String = ApiFootballConfig.PREMIER_LEAGUE_QUALIFIER_COUNTRY
) : Parameter {
    override val name: String
        get() = "Ccd"
}

@JvmInline
value class League(override val value: String = ApiFootballConfig.PREMIER_LEAGUE_QUALIFIER_NAME) : Parameter {
    override val name: String
        get() = "Scd"
}