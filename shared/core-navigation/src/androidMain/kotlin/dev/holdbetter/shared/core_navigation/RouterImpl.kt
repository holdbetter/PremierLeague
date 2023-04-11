package dev.holdbetter.shared.core_navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import dev.holdbetter.shared.core_navigation.Destination.Standings.route
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal class RouterImpl(
    private val controller: NavigationController
) : Router {

    override fun handleThirdPartyLink(uri: String) {
        val context = controller.context
        try {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            ).also(context::startActivity)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.device_not_supported, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun navigateToStandings() {
        controller.navigate(Destination.Standings.route())
    }

    override fun navigateToTeam(teamId: Long, teamImageUrl: String) {
        val encodedUrl = URLEncoder.encode(teamImageUrl, StandardCharsets.UTF_8.toString())
        controller.navigate(Destination.TeamDetail.route(teamId, encodedUrl))
    }

    override fun back() {
        controller.navigateUp()
    }
}