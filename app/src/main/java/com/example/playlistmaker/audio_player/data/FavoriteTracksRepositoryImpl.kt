package com.example.playlistmaker.audio_player.data

import com.example.playlistmaker.audio_player.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.library.data.data_source.FavoriteTracksDataSource
import com.example.playlistmaker.library.data.db.FavoriteTrackEntity
import com.example.playlistmaker.library.data.mapper.FavoriteTrackEntityToTrackMapper
import com.example.playlistmaker.library.data.mapper.TrackToFavoriteTrackEntityMapper
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(
    private val dataSource: FavoriteTracksDataSource,
    private val trackToFavoriteTrackEntityMapper: TrackToFavoriteTrackEntityMapper,
    private val favoriteTrackEntityToTrackMapper: FavoriteTrackEntityToTrackMapper,
) : FavoriteTracksRepository {

    override suspend fun addToFavorites(track: Track) {
        val favoriteTrackEntity = trackToFavoriteTrackEntityMapper.execute(track)
        dataSource.addFavoriteTrack(favoriteTrackEntity)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        val favoriteTrackEntity = trackToFavoriteTrackEntityMapper.execute(track)
        dataSource.deleteFromFavorites(favoriteTrackEntity)
    }

    override suspend fun getAllFavorites(): Flow<List<Track>> {
        return dataSource.getAllFavoriteTracksFlow().map { tracksList: List<FavoriteTrackEntity> ->
            tracksList.map { favoriteTracksEntity: FavoriteTrackEntity ->
                favoriteTrackEntityToTrackMapper.execute(favoriteTracksEntity)
            }
        }
    }

    override suspend fun getAllFavoritesIdFlow(): Flow<List<Long>> {
        return dataSource.getAllFavoriteTracksIdFlow()
    }

    override suspend fun getAllFavoritesId(): List<Long> {
        return dataSource.getAllFavoriteTrackId()
    }
}