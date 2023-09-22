package com.example.playlistmaker.createPlaylist.di

import com.example.playlistmaker.createPlaylist.data.ImageRepositoryImpl
import com.example.playlistmaker.createPlaylist.data.PlaylistRepositoryImpl
import com.example.playlistmaker.createPlaylist.data.dataSource.ImagePrivateDataSource
import com.example.playlistmaker.createPlaylist.data.dataSource.PlaylistsDataSource
import com.example.playlistmaker.createPlaylist.data.dataSourceImpl.ImagePrivateDataSourceImpl
import com.example.playlistmaker.createPlaylist.data.dataSourceImpl.PlaylistDataSourceImpl
import com.example.playlistmaker.createPlaylist.data.mapper.PlaylistModelToPlaylistEntityMapper
import com.example.playlistmaker.createPlaylist.data.mapper.PlaylistWithSongToPlaylistModelMapper
import com.example.playlistmaker.createPlaylist.data.mapper.TrackEntityToTrackMapper
import com.example.playlistmaker.createPlaylist.data.mapper.TrackToTracksEntityMapper
import com.example.playlistmaker.createPlaylist.domain.repository.ImageRepository
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val createPlaylistDataModule = module {

    singleOf(::PlaylistDataSourceImpl) { bind<PlaylistsDataSource>() }

    singleOf(::ImagePrivateDataSourceImpl) { bind<ImagePrivateDataSource>() }

    factoryOf(::TrackEntityToTrackMapper)

    factoryOf(::TrackToTracksEntityMapper)

    factoryOf(::PlaylistModelToPlaylistEntityMapper)

    factoryOf(::PlaylistWithSongToPlaylistModelMapper)

    singleOf(::PlaylistRepositoryImpl) { bind<PlayListRepository>() }

    singleOf(::ImageRepositoryImpl) { bind<ImageRepository>() }

}