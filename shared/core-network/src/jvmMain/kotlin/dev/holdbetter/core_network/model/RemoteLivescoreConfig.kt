package dev.holdbetter.core_network.model

object RemoteLivescoreConfig {
    const val SERVICE_NAME = "livescore"
    const val CLIENT = "https://livescore6.p.rapidapi.com"
    const val IMAGE_HOST = "https://lsm-static-prod.livescore.com/high/"

    const val API_KEY_HEADER = "X-RapidAPI-Key"
    const val API_HOST_HEADER = "X-RapidAPI-Host"

    const val PREMIER_LEAGUE_ID = "10765"
    const val SPORT_CATEGORY = "soccer"
    const val PREMIER_LEAGUE_QUALIFIER_NAME = "premier-league"
    const val PREMIER_LEAGUE_QUALIFIER_COUNTRY = "england"
    const val PREMIER_LEAGUE_QUALIFIER_TIMEZONE = "0"

    object Paths {
        const val UNIVERSAL_DATA = "/matches/v2/list-by-league"
    }
}