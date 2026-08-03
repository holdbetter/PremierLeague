package dev.holdbetter.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import dev.holdbetter.compose.design_system.LeagueTheme
import dev.holdbetter.core_network.di.IosClientGraph
import dev.holdbetter.core_network.di.NetworkGraph
import dev.holdbetter.shared.core_database.di.IosDatabaseGraph
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metro.createGraphFactory
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.UIKit.UIViewController
import kotlin.experimental.ExperimentalNativeApi

@Suppress("unused") // Called from Swift
fun initApp() {
    installNativeExceptionLogging()
    appGraph
    Napier.base(DebugAntilog())
    Napier.d { "VERSION ${BuildInfo.VERSION}" }
}

@OptIn(ExperimentalNativeApi::class)
fun installNativeExceptionLogging() {
    setUnhandledExceptionHook { throwable ->
        Napier.d {
            "Throws: $throwable \n" +
            "Trace: ${throwable.stackTraceToString()}"
        }

        terminateWithUnhandledException(throwable)
    }
}

private val appGraph = createGraphFactory<IosAppGraph.Factory>()
    .create(
        databaseGraph = createGraph<IosDatabaseGraph>(),
        networkGraph = createGraphFactory<NetworkGraph.Factory>()
            .create(
                clientModule = createGraph<IosClientGraph>()
            )
    )

private val StandingsRoute by appGraph::standingsRoute

@Suppress("unused") // Called from Swift
fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = { onFocusBehavior = OnFocusBehavior.DoNothing },
) {
    LeagueTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            StandingsRoute()
        }
    }
}

object BuildInfo {
    const val VERSION = 1
}