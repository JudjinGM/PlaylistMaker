package com.example.playlistmaker.sharing.domain.repository

import com.example.playlistmaker.sharing.data.model.EmailData

interface ShareResourceRepository {
    fun getShareAppLink(): String
    fun getTermsLink(): String
    fun getEmailData(): EmailData
}