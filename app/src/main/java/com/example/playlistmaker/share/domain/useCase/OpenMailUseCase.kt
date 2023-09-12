package com.example.playlistmaker.share.domain.useCase

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