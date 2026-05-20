package dev.holdbetter.core_network.model

@JvmInline
value class Category(override val value: String = RemoteLivescoreConfig.SPORT_CATEGORY) :
    Parameter {
    override val name: String
        get() = "Category"
}

@JvmInline
value class Country(
    override val value: String = RemoteLivescoreConfig.PREMIER_LEAGUE_QUALIFIER_COUNTRY
) : Parameter {
    override val name: String
        get() = "Ccd"
}

@JvmInline
value class League(override val value: String = RemoteLivescoreConfig.PREMIER_LEAGUE_QUALIFIER_NAME) :
    Parameter {
    override val name: String
        get() = "Scd"
}

@JvmInline
value class Timezone(override val value: String = RemoteLivescoreConfig.PREMIER_LEAGUE_QUALIFIER_TIMEZONE) :
    Parameter {
    override val name: String
        get() = "Timezone"
}