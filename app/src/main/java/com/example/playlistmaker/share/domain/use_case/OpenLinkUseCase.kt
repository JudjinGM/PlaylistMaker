package com.example.playlistmaker.share.domain.use_case

import com.example.playlistmaker.share.domain.navigator.ExternalNavigator

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