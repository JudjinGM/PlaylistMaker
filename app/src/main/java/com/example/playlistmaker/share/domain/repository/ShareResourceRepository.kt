package com.example.playlistmaker.share.domain.repository

import com.example.playlistmaker.share.data.model.EmailData

interface ShareResourceRepository {
    fun getShareAppLink(): String
    fun getTermsLink(): String
    fun getEmailData(): EmailData
}