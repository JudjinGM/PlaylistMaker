package com.example.playlistmaker.share.di

import com.example.playlistmaker.share.domain.useCase.OpenLinkUseCase
import com.example.playlistmaker.share.domain.useCase.OpenMailUseCase
import com.example.playlistmaker.share.domain.useCase.ShareLinkUseCase
import org.koin.dsl.module

val shareDomainModule = module {

    factory<OpenMailUseCase> {
        OpenMailUseCase.Base(externalNavigator = get())
    }

    factory<OpenLinkUseCase> {
        OpenLinkUseCase.Base(externalNavigator = get())
    }

    factory<ShareLinkUseCase> {
        ShareLinkUseCase.Base(externalNavigator = get())
    }
}