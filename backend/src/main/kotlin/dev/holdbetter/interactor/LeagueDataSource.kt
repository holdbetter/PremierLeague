package dev.holdbetter.interactor

import dev.holdbetter.core_network.DataSource.Database
import dev.holdbetter.core_network.DataSource.Network
import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import dev.holdbetter.innerApi.model.DayLimit
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

internal interface LeagueDataSource : Network, Database {

    val database: DatabaseGateway
    val network: NetworkGateway

    suspend fun updateLeagueData(leagueParameter: League, countryParameter: Country): Duration
    fun calculateDelay(today: LocalDateTime, todayLimit: DayLimit, nextDayLimit: DayLimit): Duration
}