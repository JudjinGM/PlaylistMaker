package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.di.data.ShareDataSource
import com.example.playlistmaker.sharing.di.data.ShareResourceRepositoryImpl
import com.example.playlistmaker.sharing.domain.repository.ShareResourceRepository
import org.koin.dsl.module

val sharingDataModule = module {

    single<ShareDataSource> {
        ShareDataSource.Base(context = get())
    }

    single<ShareResourceRepository> {
        ShareResourceRepositoryImpl(shareDataSource = get())
    }
}