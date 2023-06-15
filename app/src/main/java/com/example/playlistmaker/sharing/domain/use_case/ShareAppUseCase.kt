package com.example.playlistmaker.sharing.domain.use_case

import com.example.playlistmaker.sharing.data.ExternalNavigator

interface ShareAppUseCase {
    fun execute()

    class Base(private val externalNavigator: ExternalNavigator) : ShareAppUseCase {
        override fun execute() {
            externalNavigator.shareLink()
        }
    }
}