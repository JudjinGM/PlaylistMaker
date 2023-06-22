package com.example.playlistmaker.share.domain.use_case

import com.example.playlistmaker.share.domain.navigator.ExternalNavigator

interface ShareLinkUseCase {
    fun execute()

    class Base(
        private val externalNavigator: ExternalNavigator
    ) : ShareLinkUseCase {
        override fun execute() {
            externalNavigator.shareLink()
        }
    }
}