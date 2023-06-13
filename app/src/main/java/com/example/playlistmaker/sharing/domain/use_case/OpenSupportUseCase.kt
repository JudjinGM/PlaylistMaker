package com.example.playlistmaker.sharing.domain.use_case

import com.example.playlistmaker.sharing.data.ExternalNavigator

interface OpenSupportUseCase {
    fun execute()

    class Base(private val externalNavigator: ExternalNavigator):OpenSupportUseCase{
        override fun execute() {
            externalNavigator.openEmail()
        }
    }
}