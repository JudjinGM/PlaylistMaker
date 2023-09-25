package com.example.playlistmaker.share.di

import com.example.playlistmaker.share.data.ShareDataSource
import com.example.playlistmaker.share.data.ShareResourceRepositoryImpl
import com.example.playlistmaker.share.domain.repository.ShareResourceRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val shareDataModule = module {

    single<ShareDataSource> {
        ShareDataSource.Base(context = get())
    }

    singleOf(::ShareResourceRepositoryImpl) { bind<ShareResourceRepository>() }
}