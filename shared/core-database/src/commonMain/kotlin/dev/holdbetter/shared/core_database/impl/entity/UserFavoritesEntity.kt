package dev.holdbetter.shared.core_database.impl.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import dev.holdbetter.shared.core_database.entity.UserFavorites

@Entity(tableName = "favorites")
data class UserFavoritesEntity(
    @PrimaryKey override val teamId: Long
) : UserFavorites(teamId)