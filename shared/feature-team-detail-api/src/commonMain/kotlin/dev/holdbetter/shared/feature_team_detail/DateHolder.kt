package dev.holdbetter.shared.feature_team_detail

import kotlinx.datetime.LocalDate

data class DateHolder(
    val date: LocalDate,
    val isColored: Boolean = false,
    val isSelected: Boolean = false
)