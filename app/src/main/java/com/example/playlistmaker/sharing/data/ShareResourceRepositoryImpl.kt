package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.data.model.EmailData
import com.example.playlistmaker.sharing.domain.repository.ShareResourceRepository

class ShareResourceRepositoryImpl(private val shareDataSource: ShareDataSource) :
    ShareResourceRepository {
    override fun getShareAppLink(): String {
        return shareDataSource.getShareAppLink()
    }

    override fun getTermsLink(): String {
        return shareDataSource.getTermsLink()
    }

    override fun getEmailData(): EmailData {
        return shareDataSource.getEmailData()
    }
}