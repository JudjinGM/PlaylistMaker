package com.example.playlistmaker.library.di

import androidx.room.Room
import com.example.playlistmaker.library.data.dataSource.FavoriteTracksDataSource
import com.example.playlistmaker.library.data.dataSourceImpl.FavoriteTracksDataSourceImpl
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.mapper.FavoriteTrackEntityToTrackMapper
import com.example.playlistmaker.library.data.mapper.TrackToFavoriteTrackEntityMapper
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val libraryDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration().build()
    }

    singleOf(::FavoriteTracksDataSourceImpl) { bind<FavoriteTracksDataSource>() }

    factoryOf(::FavoriteTrackEntityToTrackMapper)

    factoryOf(::TrackToFavoriteTrackEntityMapper)

}