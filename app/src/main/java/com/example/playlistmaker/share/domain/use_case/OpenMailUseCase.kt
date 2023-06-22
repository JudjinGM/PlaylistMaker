package com.example.playlistmaker.share.domain.use_case

import com.example.playlistmaker.share.domain.navigator.ExternalNavigator

interface OpenMailUseCase {
    fun execute()

    class Base(
        private val externalNavigator: ExternalNavigator
    ) : OpenMailUseCase {
        override fun execute() {
            externalNavigator.openMail()
        }
    }
}