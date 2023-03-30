package dev.holdbetter.shared.core_database.api

interface FavoritesApi {
    suspend fun getFavoriteTeamIds(): List<Long>
    suspend fun addFavorite(teamId: Long)
    suspend fun removeFavorite(teamId: Long)
}