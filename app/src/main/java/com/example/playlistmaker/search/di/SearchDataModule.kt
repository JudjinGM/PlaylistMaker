package com.example.playlistmaker.search.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.dataSource.TracksListenHistoryLocalDataSource
import com.example.playlistmaker.search.data.dataSource.TracksSearchLocalDataSource
import com.example.playlistmaker.search.data.dataSource.TracksSearchRemoteDataSource
import com.example.playlistmaker.search.data.dataSourceImpl.TracksListenHistoryLocalDataSourceImpl
import com.example.playlistmaker.search.data.dataSourceImpl.TracksSearchLocalDataSourceImpl
import com.example.playlistmaker.search.data.dataSourceImpl.TracksSearchRemoteDataSourceImpl
import com.example.playlistmaker.search.data.mapper.TracksDtoToListTracksMapper
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.repositoryImpl.ListenHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repositoryImpl.SearchRepositoryImpl
import com.example.playlistmaker.search.data.storage.TracksListenHistoryLocalDatabase
import com.example.playlistmaker.search.data.storage.TracksListenHistoryLocalDatabaseImpl
import com.example.playlistmaker.search.data.storage.TracksSearchCache
import com.example.playlistmaker.search.data.storage.TracksSearchCacheImpl
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository
import com.example.playlistmaker.search.domain.repository.SearchRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            TracksListenHistoryLocalDatabaseImpl.PLAYLIST_PREFS, Context.MODE_PRIVATE
        )
    }

    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    singleOf(::TracksDtoToListTracksMapper)

    singleOf(::TracksSearchRemoteDataSourceImpl) { bind<TracksSearchRemoteDataSource>() }

    singleOf(::TracksListenHistoryLocalDatabaseImpl) { bind<TracksListenHistoryLocalDatabase>() }

    singleOf(::TracksSearchCacheImpl) { bind<TracksSearchCache>() }

    singleOf(::TracksListenHistoryLocalDataSourceImpl) { bind<TracksListenHistoryLocalDataSource>() }

    singleOf(::TracksSearchLocalDataSourceImpl) { bind<TracksSearchLocalDataSource>() }

    singleOf(::ListenHistoryRepositoryImpl) { bind<ListenHistoryRepository>() }

    singleOf(::SearchRepositoryImpl) { bind<SearchRepository>() }

}