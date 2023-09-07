package com.example.playlistmaker.createPlaylist.di

import com.example.playlistmaker.createPlaylist.data.PlaylistRepositoryImpl
import com.example.playlistmaker.createPlaylist.data.dataSource.PlaylistsDataSource
import com.example.playlistmaker.createPlaylist.data.dataSourceImpl.PlaylistDataSourceImpl
import com.example.playlistmaker.createPlaylist.data.mapper.PlaylistModelToPlaylistEntityMapper
import com.example.playlistmaker.createPlaylist.data.mapper.PlaylistWithSongToPlaylistModelMapper
import com.example.playlistmaker.createPlaylist.data.mapper.TrackEntityToTrackMapper
import com.example.playlistmaker.createPlaylist.data.mapper.TrackToTracksEntityMapper
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository
import org.koin.dsl.module

val createPlaylistDataModule = module {

    single<PlaylistsDataSource> {
        PlaylistDataSourceImpl(dataBase = get())
    }


    factory { TrackEntityToTrackMapper() }
    factory { TrackToTracksEntityMapper() }
    factory { PlaylistModelToPlaylistEntityMapper() }
    factory { PlaylistWithSongToPlaylistModelMapper(get()) }

    single<PlayListRepository> {
        PlaylistRepositoryImpl(
            dataSource = get(),
            favoriteTracksDataSource = get(),
            playlistModelToPlaylistEntityMapper = get(),
            playlistWithSongToPlaylistModelMapper = get(),
            trackToTracksEntityMapper = get()
        )
    }
}