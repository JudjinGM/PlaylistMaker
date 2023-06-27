package com.example.playlistmaker.share.di

import com.example.playlistmaker.share.data.ShareDataSource
import com.example.playlistmaker.share.data.ShareResourceRepositoryImpl
import com.example.playlistmaker.share.domain.repository.ShareResourceRepository
import org.koin.dsl.module

val shareDataModule = module {

    single<ShareDataSource> {
        ShareDataSource.Base(context = get())
    }

    single<ShareResourceRepository> {
        ShareResourceRepositoryImpl(shareDataSource = get())
    }
}