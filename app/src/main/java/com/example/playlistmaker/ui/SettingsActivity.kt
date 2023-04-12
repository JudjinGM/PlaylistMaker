package com.example.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.data.local.database.LocalDatabase.Companion.APP_THEME_STATUS

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backImageView = findViewById<ImageView>(R.id.iconBack)
        backImageView.setOnClickListener {
            finish()
        }

        val shareImageView = findViewById<ImageView>(R.id.image_share)
        val shareTextView = findViewById<TextView>(R.id.text_share_app)

        val shareClickListener = View.OnClickListener {
            val shareText = getString(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = INTENT_EMAIL_TYPE
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(shareIntent, CHOOSER_TITLE))
        }

        shareImageView.setOnClickListener(shareClickListener)
        shareTextView.setOnClickListener(shareClickListener)

        val supportImageView = findViewById<ImageView>(R.id.image_support)
        val supportTextView = findViewById<TextView>(R.id.text_support)

        val supportClickListener = View.OnClickListener {
            val subject = getString(R.string.mail_to_support_subject)
            val message = getString(R.string.mail_to_support_message)
            val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
            val mailAddress = getString(R.string.developer_email_address)
            writeToSupportIntent.data = Uri.parse("mailto:")
            writeToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailAddress))
            writeToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            writeToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(writeToSupportIntent)
        }

        supportImageView.setOnClickListener(supportClickListener)
        supportTextView.setOnClickListener(supportClickListener)

        val agreementImageView = findViewById<ImageView>(R.id.image_term)
        val agreementTextView = findViewById<TextView>(R.id.text_term)

        val agreementClickListener = View.OnClickListener {
            val link = getString(R.string.link_to_agreement)
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(link)
            startActivity(agreementIntent)
        }

        agreementImageView.setOnClickListener(agreementClickListener)
        agreementTextView.setOnClickListener(agreementClickListener)

        val themeSwitcher = findViewById<SwitchCompat>(R.id.switch_to_dark_theme)

        themeSwitcher.isChecked =
            App.settingsRepository.loadBooleanSetting(
                APP_THEME_STATUS,
                App.isDarkThemeEnabled(this)
            )

        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            if (isChecked) {
                App.settingsRepository.saveBooleanSetting(APP_THEME_STATUS, true)
            } else {
                App.settingsRepository.saveBooleanSetting(APP_THEME_STATUS, false)
            }
        }
    }

    private companion object {
        const val INTENT_EMAIL_TYPE = "text/plain"
        const val CHOOSER_TITLE = "Share"
    }
}