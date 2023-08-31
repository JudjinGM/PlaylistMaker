package com.example.playlistmaker.search.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.data_source.TracksListenHistoryLocalDataSource
import com.example.playlistmaker.search.data.data_source.TracksSearchLocalDataSource
import com.example.playlistmaker.search.data.data_source.TracksSearchRemoteDataSource
import com.example.playlistmaker.search.data.data_source_impl.TracksListenHistoryLocalDataSourceImpl
import com.example.playlistmaker.search.data.data_source_impl.TracksSearchLocalDataSourceImpl
import com.example.playlistmaker.search.data.data_source_impl.TracksSearchRemoteDataSourceImpl
import com.example.playlistmaker.search.data.mapper.TracksDtoToListTracksMapper
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.repository_impl.ListenHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository_impl.SearchRepositoryImpl
import com.example.playlistmaker.search.data.storage.TracksListenHistoryLocalDatabase
import com.example.playlistmaker.search.data.storage.TracksListenHistoryLocalDatabaseImpl
import com.example.playlistmaker.search.data.storage.TracksSearchCache
import com.example.playlistmaker.search.data.storage.TracksSearchCacheImpl
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository
import com.example.playlistmaker.search.domain.repository.SearchRepository
import org.koin.android.ext.koin.androidContext
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

    single {
        TracksDtoToListTracksMapper()
    }

    single<TracksSearchRemoteDataSource> {
        TracksSearchRemoteDataSourceImpl(
            itunesService = get()
        )
    }

    single<TracksListenHistoryLocalDatabase> {
        TracksListenHistoryLocalDatabaseImpl(playlistSharedPreferences = get())
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
        ListenHistoryRepositoryImpl(listenHistoryLocalDataSource = get(), favoriteTracksDataSource = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(remoteDataSource = get(), searchLocalDataSource = get(), mapper = get(), favoriteTrackDataSource = get())
    }
}