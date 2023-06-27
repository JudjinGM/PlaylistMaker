package com.example.playlistmaker.share.data

import com.example.playlistmaker.share.data.model.EmailData
import com.example.playlistmaker.share.domain.repository.ShareResourceRepository

class ShareResourceRepositoryImpl(
    private val shareDataSource: ShareDataSource
) : ShareResourceRepository {
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