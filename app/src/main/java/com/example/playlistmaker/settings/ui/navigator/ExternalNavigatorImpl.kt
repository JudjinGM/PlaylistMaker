package com.example.playlistmaker.settings.ui.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.share.domain.navigator.ExternalNavigator
import com.example.playlistmaker.share.domain.repository.ShareResourceRepository
import java.text.SimpleDateFormat
import java.util.Locale

class ExternalNavigatorImpl(
    private val context: Context, private val shareResourceRepository: ShareResourceRepository
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

    override fun sharePlaylist(playlistModel: PlaylistModel) {

        val playlistName = playlistModel.playlistName
        val playlistDescription = playlistModel.playlistDescription
        var playlistNameAndDescription = ""
        playlistNameAndDescription += if (playlistDescription.isEmpty()) {
            playlistName
        } else {
            playlistName + "\n" + playlistDescription
        }
        playlistNameAndDescription += "\n" + context.resources.getQuantityString(
            R.plurals.tracks_plural, playlistModel.tracks.size, playlistModel.tracks.size
        )
        var playlistTracks = ""

        playlistModel.tracks.forEachIndexed { index, track ->
            playlistTracks += "\n${index + 1}. ${track.artistName} - ${track.trackName} ${
                SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(track.trackTimeMillis)
            }"
        }

        val playlistTextToShare = playlistNameAndDescription + playlistTracks

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = INTENT_EMAIL_TYPE
        shareIntent.putExtra(Intent.EXTRA_TEXT, playlistTextToShare)

        val chooserIntent = Intent.createChooser(shareIntent, CHOOSER_TITLE)
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooserIntent)
    }

    private companion object {
        const val INTENT_EMAIL_TYPE = "text/plain"
        const val CHOOSER_TITLE = "Share"
        const val MAIL_TO = "mailto:"
    }
}