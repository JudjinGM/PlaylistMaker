package com.example.playlistmaker.sharing.domain.use_case

import com.example.playlistmaker.sharing.domain.navigator.ExternalNavigator

interface OpenLinkUseCase {
    fun execute()

    class Base(
        private val externalNavigator: ExternalNavigator
    ) : OpenLinkUseCase {
        override fun execute() {
            externalNavigator.openLink()
        }
    }
}