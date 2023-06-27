package com.example.playlistmaker.share.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.share.data.model.EmailData

interface ShareDataSource {
    fun getShareAppLink(): String

    fun getTermsLink(): String

    fun getEmailData(): EmailData

    class Base(private val context: Context) : ShareDataSource {
        override fun getShareAppLink(): String {
            return context.getString(R.string.share_link)
        }

        override fun getTermsLink(): String {
            return context.getString(R.string.link_to_agreement)
        }

        override fun getEmailData(): EmailData {
            return EmailData(
                subject = context.getString(R.string.mail_to_support_subject),
                message = context.getString(R.string.mail_to_support_message),
                emailAddress = context.getString(R.string.developer_email_address)
            )
        }
    }
}