package dev.holdbetter.presenter

import dev.holdbetter.common.util.decodeWith
import dev.holdbetter.core_network.NetworkInteractor
import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import dev.holdbetter.core_network.model.RemoteLivescoreConfig
import dev.holdbetter.interactor.NetworkGateway
import dev.holdbetter.outerApi.util.LivescoreUnwrapper
import kotlinx.serialization.json.Json

internal class NetworkGatewayImpl(
    override val decoder: Json,
    override val networkInteractor: NetworkInteractor,
    private val livescoreUnwrapper: LivescoreUnwrapper,
) : NetworkGateway {

    override val paths: Array<String> = arrayOf(
        RemoteLivescoreConfig.Paths.UNIVERSAL_DATA
    )

    // TODO: Exception handling
    override suspend fun getLeague(
        league: League,
        country: Country
    ) = networkInteractor.get(paths, league, country).run {
        decoder.decodeWith(this, livescoreUnwrapper)
    }
}