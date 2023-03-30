package dev.holdbetter.shared.core_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.holdbetter.shared.core_database.api.FavoritesApi
import dev.holdbetter.shared.core_database.entity.UserFavoritesEntity

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