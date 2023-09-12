package com.example.playlistmaker.library.data.dataSourceImpl

import com.example.playlistmaker.library.data.dataSource.FavoriteTracksDataSource
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

class FavoriteTracksDataSourceImpl(
    private val database: AppDatabase
) : FavoriteTracksDataSource {

    override fun getAllFavoriteTracksFlow(): Flow<List<FavoriteTrackEntity>> {
        return database.favoriteTracksDao().getFavoriteTracksFlow()
    }

    override fun getAllFavoriteTracksIdFlow(): Flow<List<Long>> {
        return database.favoriteTracksDao().getFavoritesTracksIdFlow()
    }

    override suspend fun getAllFavoriteTrackId(): List<Long> {
        return database.favoriteTracksDao().getFavoritesTracksId()
    }

    override suspend fun addFavoriteTrack(trackEntity: FavoriteTrackEntity) {
        database.favoriteTracksDao().insertFavoriteTrack(trackEntity)
    }

    override suspend fun deleteFromFavorites(trackEntity: FavoriteTrackEntity) {
        database.favoriteTracksDao().deleteFavoriteTrack(trackEntity)
    }
}