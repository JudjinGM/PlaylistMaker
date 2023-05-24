package com.example.playlistmaker.domain.usecases

import android.content.Context
import android.content.res.Configuration

class CheckPhoneThemeUseCase {
    fun execute(context: Context):Boolean{
        val result =
            when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
        return result
    }
}