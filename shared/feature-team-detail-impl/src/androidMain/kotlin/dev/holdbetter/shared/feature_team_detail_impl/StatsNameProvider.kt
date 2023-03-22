package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context

internal class StatsNameProvider(private val context: Context) {

    private val resources
        get() = context.resources


    val gamesName by lazy {
        resources.getString(R.string.games)
    }

    val wonName by lazy {
        resources.getString(R.string.won)
    }

    val drawsName by lazy {
        resources.getString(R.string.draws)
    }

    val losesName by lazy {
        resources.getString(R.string.loses)
    }

    val goalsForName by lazy {
        resources.getString(R.string.goals_for)
    }

    val goalsAgainstName by lazy {
        resources.getString(R.string.goals_against)
    }
}