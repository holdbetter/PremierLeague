package dev.holdbetter.routes

interface Parameter  {
    val value: String
    val name: String
}

@JvmInline
value class Season(override val value: String) : Parameter {
    override val name: String
        get() = "season"
}

@JvmInline
value class League(override val value: String = ApiFootballConfig.PREMIER_LEAGUE_ID) : Parameter {
    override val name: String
        get() = "league"
}