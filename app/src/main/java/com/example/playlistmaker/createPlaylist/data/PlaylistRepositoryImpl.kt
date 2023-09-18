package com.example.playlistmaker.createPlaylist.data

import com.example.playlistmaker.createPlaylist.data.dataSource.PlaylistsDataSource
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistWithSongs
import com.example.playlistmaker.createPlaylist.data.mapper.PlaylistModelToPlaylistEntityMapper
import com.example.playlistmaker.createPlaylist.data.mapper.PlaylistWithSongToPlaylistModelMapper
import com.example.playlistmaker.createPlaylist.data.mapper.TrackToTracksEntityMapper
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository
import com.example.playlistmaker.library.data.dataSource.FavoriteTracksDataSource
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val dataSource: PlaylistsDataSource,
    private val favoriteTracksDataSource: FavoriteTracksDataSource,
    private val playlistWithSongToPlaylistModelMapper: PlaylistWithSongToPlaylistModelMapper,
    private val playlistModelToPlaylistEntityMapper: PlaylistModelToPlaylistEntityMapper,
    private val trackToTracksEntityMapper: TrackToTracksEntityMapper

) : PlayListRepository {
    override suspend fun createPlaylist(playlistModel: PlaylistModel) {
        dataSource.addPlaylist(playlistModelToPlaylistEntityMapper.execute(playlistModel))
    }

    override suspend fun deletePlaylist(playlistId: Long, playlistModel: PlaylistModel) {
        dataSource.removePlaylist(
            playlistModelToPlaylistEntityMapper.execute(
                playlistModel,
                playlistId
            )
        )
    }

    override suspend fun addTrackToPlayList(playlistId: Long, track: Track) {
        dataSource.addTrackToPlaylist(playlistId, trackToTracksEntityMapper.execute(track))
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, track: Track) {
        dataSource.removeTrackFromPlaylist(playlistId, trackToTracksEntityMapper.execute(track))
    }

    override fun getPlaylistByIdFlow(playlistId: Long): Flow<PlaylistModel> {
        return dataSource.getPlaylistByIdFlow(playlistId).map { playlistWithSongs ->
            val playlistModel = playlistWithSongToPlaylistModelMapper.execute(playlistWithSongs)
            playlistModel.tracks.forEach {
                if (favoriteTracksDataSource.getAllFavoriteTrackId().contains(it.trackId)) {
                    it.isFavorite = true
                }
            }
            playlistModel
        }
    }

    override fun getAllPlaylistsFlow(): Flow<List<PlaylistModel>> {
        return dataSource.getAllPlaylists().map { playlistWithSongsList: List<PlaylistWithSongs> ->
            playlistWithSongsList.map { playlistWithSongs ->
                val playlistModel = playlistWithSongToPlaylistModelMapper.execute(playlistWithSongs)
                playlistModel.tracks.forEach {
                    if (favoriteTracksDataSource.getAllFavoriteTrackId().contains(it.trackId)) {
                        it.isFavorite = true
                    }
                }
                playlistModel
            }
        }
    }
}