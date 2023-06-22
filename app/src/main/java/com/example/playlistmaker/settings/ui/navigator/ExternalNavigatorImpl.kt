package com.example.playlistmaker.settings.ui.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.domain.navigator.ExternalNavigator
import com.example.playlistmaker.sharing.domain.repository.ShareResourceRepository

class ExternalNavigatorImpl(
    private val context: Context,
    private val shareResourceRepository: ShareResourceRepository
) : ExternalNavigator {
    override fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = INTENT_EMAIL_TYPE
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareResourceRepository.getShareAppLink())

        val chooserIntent = Intent.createChooser(shareIntent, CHOOSER_TITLE)
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooserIntent)
    }

    override fun openMail() {
        val emailData = shareResourceRepository.getEmailData()
        val subject = emailData.subject
        val message = emailData.message
        val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
        val mailAddress = emailData.emailAddress
        writeToSupportIntent.data = Uri.parse(MAIL_TO)
        writeToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailAddress))
        writeToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        writeToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
        writeToSupportIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(writeToSupportIntent)
    }

    override fun openLink() {
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        agreementIntent.data = Uri.parse(shareResourceRepository.getTermsLink())
        agreementIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(agreementIntent)
    }

    private companion object {
        const val INTENT_EMAIL_TYPE = "text/plain"
        const val CHOOSER_TITLE = "Share"
        const val MAIL_TO = "mailto:"
    }
}