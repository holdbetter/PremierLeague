package dev.holdbetter.shared.feature_team_detail

import dev.holdbetter.common.Season
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

fun initCalendar(season: IntRange = Season.value): List<DateHolder> {
    val mutableDates = mutableListOf<DateHolder>()

    for (year in season) {
        var dateToAdd = LocalDate(year, 1, 1)
        while (dateToAdd.year == year) {
            mutableDates.add(DateHolder(dateToAdd))
            dateToAdd = dateToAdd.plus(1, DateTimeUnit.DAY)
        }
    }

    return mutableDates
}