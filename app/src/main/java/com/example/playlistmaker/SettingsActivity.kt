package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private val intentEmailType = "text/plain"
    private val chooserTitle = "Share"
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
            shareIntent.type = intentEmailType
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(shareIntent, chooserTitle))
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
    }
}