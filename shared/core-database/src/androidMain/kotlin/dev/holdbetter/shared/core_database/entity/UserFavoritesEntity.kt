package dev.holdbetter.shared.core_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class UserFavoritesEntity(
    @PrimaryKey override val teamId: Long
) : UserFavorites(teamId)