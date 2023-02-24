package dev.holdbetter.network

object ApiFootballConfig {
    const val SERVICE_NAME = "livescore"
    const val HOST = "livescore6.p.rapidapi.com"

    const val API_KEY_HEADER = "X-RapidAPI-Key"
    const val API_HOST_HEADER = "X-RapidAPI-Host"

    const val PREMIER_LEAGUE_ID = "10765"
    const val SPORT_CATEGORY = "soccer"
    const val PREMIER_LEAGUE_QUALIFIER_NAME = "premier-league"
    const val PREMIER_LEAGUE_QUALIFIER_COUNTRY = "england"

    object Paths {
        const val UNIVERSAL_DATA = "/matches/v2/list-by-league"
    }
}