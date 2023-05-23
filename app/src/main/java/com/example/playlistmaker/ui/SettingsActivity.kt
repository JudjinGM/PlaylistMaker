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
import com.example.playlistmaker.data.storage.SettingsLocalDatabase.Companion.APP_THEME_STATUS
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.SetThemeUseCase

class SettingsActivity : AppCompatActivity() {
    private lateinit var backImageView: ImageView
    private lateinit var shareImageView: ImageView
    private lateinit var shareTextView: TextView
    private lateinit var supportImageView: ImageView
    private lateinit var supportTextView: TextView
    private lateinit var agreementImageView: ImageView
    private lateinit var agreementTextView: TextView
    private lateinit var themeSwitcher: SwitchCompat

    private lateinit var getThemeUseCase: GetThemeUseCase
    private lateinit var setThemeUseCase: SetThemeUseCase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        getThemeUseCase = GetThemeUseCase(settingsRepository = App.settingsRepository, this)
        setThemeUseCase = SetThemeUseCase()

        contentInit()
        setOnClicks()

    }

    private fun contentInit() {
        backImageView = findViewById(R.id.backSettingImageView)
        shareImageView = findViewById(R.id.image_share)
        shareTextView = findViewById(R.id.text_share_app)
        supportImageView = findViewById(R.id.image_support)
        supportTextView = findViewById(R.id.text_support)
        agreementImageView = findViewById(R.id.image_term)
        agreementTextView = findViewById(R.id.text_term)
        themeSwitcher = findViewById(R.id.switch_to_dark_theme)

        themeSwitcher.isChecked = getThemeUseCase.execute()
    }

    private fun setOnClicks() {
        backImageView.setOnClickListener {
            finish()
        }

        val shareClickListener = View.OnClickListener {
            val shareText = getString(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = INTENT_EMAIL_TYPE
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(shareIntent, CHOOSER_TITLE))
        }

        shareImageView.setOnClickListener(shareClickListener)
        shareTextView.setOnClickListener(shareClickListener)


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


        val agreementClickListener = View.OnClickListener {
            val link = getString(R.string.link_to_agreement)
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(link)
            startActivity(agreementIntent)
        }

        agreementImageView.setOnClickListener(agreementClickListener)
        agreementTextView.setOnClickListener(agreementClickListener)

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            setThemeUseCase.execute(isChecked)
            if (isChecked) {
                App.settingsRepository.setSettingBoolean(APP_THEME_STATUS, true)
            } else {
                App.settingsRepository.setSettingBoolean(APP_THEME_STATUS, false)
            }
        }
    }

    private companion object {
        const val INTENT_EMAIL_TYPE = "text/plain"
        const val CHOOSER_TITLE = "Share"
    }
}