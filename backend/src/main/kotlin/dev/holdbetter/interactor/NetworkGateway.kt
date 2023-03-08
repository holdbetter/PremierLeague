package dev.holdbetter.interactor

import dev.holdbetter.core_network.DataSource
import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import dev.holdbetter.outerApi.model.LivescoreDataResponse

internal interface NetworkGateway : DataSource.Network {
    suspend fun getLeague(league: League, country: Country): LivescoreDataResponse
}