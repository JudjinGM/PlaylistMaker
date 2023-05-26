package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.presenter.creators.SettingsPresenterCreator
import com.example.playlistmaker.presenter.settings.SettingsPresenter
import com.example.playlistmaker.presenter.settings.SettingsView

class SettingsActivity : AppCompatActivity(), SettingsView {
    private lateinit var backImageView: ImageView
    private lateinit var shareImageView: ImageView
    private lateinit var shareTextView: TextView
    private lateinit var supportImageView: ImageView
    private lateinit var supportTextView: TextView
    private lateinit var agreementImageView: ImageView
    private lateinit var agreementTextView: TextView
    private lateinit var themeSwitcher: SwitchCompat

    private lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val creator = SettingsPresenterCreator(this)
        presenter = creator.createPresenter(this)

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
        presenter.setSwitchCompatTheme()
    }

    private fun setOnClicks() {
        backImageView.setOnClickListener {
            finish()
        }

        val shareClickListener = View.OnClickListener {
            presenter.shareApp()
        }

        shareImageView.setOnClickListener(shareClickListener)
        shareTextView.setOnClickListener(shareClickListener)


        val supportClickListener = View.OnClickListener {
            presenter.writeToSupport()
        }

        supportImageView.setOnClickListener(supportClickListener)
        supportTextView.setOnClickListener(supportClickListener)

        val agreementClickListener = View.OnClickListener {
            presenter.showTermOfUse()
        }

        agreementImageView.setOnClickListener(agreementClickListener)
        agreementTextView.setOnClickListener(agreementClickListener)

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            presenter.changeTheme(isChecked)
        }
    }

    override fun shareApp() {
        val shareText = getString(R.string.share_link)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = INTENT_EMAIL_TYPE
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(shareIntent, CHOOSER_TITLE))
    }

    override fun writeToSupport() {
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

    override fun showTermOfUse() {
        val link = getString(R.string.link_to_agreement)
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        agreementIntent.data = Uri.parse(link)
        startActivity(agreementIntent)
    }

    override fun setSwitchCompatTheme(isChecked: Boolean) {
        themeSwitcher.isChecked = isChecked
    }

    private companion object {
        const val INTENT_EMAIL_TYPE = "text/plain"
        const val CHOOSER_TITLE = "Share"
    }
}
