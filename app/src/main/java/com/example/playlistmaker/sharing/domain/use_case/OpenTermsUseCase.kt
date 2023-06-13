package com.example.playlistmaker.sharing.domain.use_case

import com.example.playlistmaker.sharing.data.ExternalNavigator

interface OpenTermsUseCase {
    fun execute()

    class Base(private val externalNavigator: ExternalNavigator): OpenTermsUseCase {
        override fun execute() {
            externalNavigator.openTerms()
        }
    }
}