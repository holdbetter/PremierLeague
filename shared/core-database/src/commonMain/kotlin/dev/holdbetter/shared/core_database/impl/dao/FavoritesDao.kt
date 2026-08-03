package dev.holdbetter.shared.core_database.impl.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import dev.holdbetter.shared.core_database.api.FavoritesApi
import dev.holdbetter.shared.core_database.impl.entity.UserFavoritesEntity

@Dao
abstract class FavoritesDao : FavoritesApi {

    @Query("SELECT * FROM favorites")
    abstract suspend fun getFavoritesEntity(): List<UserFavoritesEntity>

    @Insert
    abstract suspend fun addFavorite(teamId: UserFavoritesEntity)

    @Delete
    abstract suspend fun removeFavorite(teamId: UserFavoritesEntity)

    override suspend fun getFavoriteTeamIds(): List<Long> = getFavoritesEntity().map { it.teamId }
    override suspend fun addFavorite(teamId: Long) = addFavorite(UserFavoritesEntity(teamId))
    override suspend fun removeFavorite(teamId: Long) = removeFavorite(UserFavoritesEntity(teamId))
}