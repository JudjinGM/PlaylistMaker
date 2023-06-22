package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.data_source.TracksListenHistoryLocalDataSource
import com.example.playlistmaker.search.data.data_source.TracksSearchRemoteDataSource
import com.example.playlistmaker.search.data.data_source.TracksSearchLocalDataSource
import com.example.playlistmaker.search.data.data_source_impl.TracksListenHistoryLocalDataSourceImpl
import com.example.playlistmaker.search.data.data_source_impl.TracksSearchLocalDataSourceImpl
import com.example.playlistmaker.search.data.data_source_impl.TracksSearchRemoteDataSourceImpl
import com.example.playlistmaker.search.data.mapper.TracksDtoToListTracksMapper
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.RetrofitFactory
import com.example.playlistmaker.search.data.repository_impl.ListenHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository_impl.SearchRepositoryImpl
import com.example.playlistmaker.search.data.storage.TracksListenHistoryLocalDatabase
import com.example.playlistmaker.search.data.storage.TracksListenHistoryLocalDatabaseImpl
import com.example.playlistmaker.search.data.storage.TracksSearchCache
import com.example.playlistmaker.search.data.storage.TracksSearchCacheImpl
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository
import com.example.playlistmaker.search.domain.repository.SearchRepository
import org.koin.dsl.module

val searchDataModule = module {

    single<ItunesApi> {
        val retrofitFactory = RetrofitFactory()
        retrofitFactory.getService()
    }

    single<TracksDtoToListTracksMapper>{
        TracksDtoToListTracksMapper()
    }

    single<TracksSearchRemoteDataSource> {
        TracksSearchRemoteDataSourceImpl(
            itunesService = get(),
            tracksDtoToTracksMapper = get()
        )
    }

    single<TracksListenHistoryLocalDatabase> {
        TracksListenHistoryLocalDatabaseImpl(context = get())
    }

    single<TracksSearchCache> {
        TracksSearchCacheImpl()
    }

    single<TracksListenHistoryLocalDataSource> {
        TracksListenHistoryLocalDataSourceImpl(database = get())
    }

    single<TracksSearchLocalDataSource> {
        TracksSearchLocalDataSourceImpl(database = get())
    }

    single<ListenHistoryRepository> {
        ListenHistoryRepositoryImpl(listenHistoryLocalDataSource = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(remoteDataSource = get(), searchLocalDataSource = get())
    }
}