package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageView>(R.id.iconBack)
        buttonBack.setOnClickListener {
            finish()
        }
        val buttonShare = findViewById<ImageView>(R.id.image_share)
        buttonShare.setOnClickListener {
            val shareText = getText(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(shareIntent, "Share" ))
        }
        val buttonSupport = findViewById<ImageView>(R.id.image_support)
        buttonSupport.setOnClickListener {
            val subject = getText(R.string.mail_to_support_subject)
            val message = getText(R.string.mail_to_support_message)
            val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
            val mailAddress = getText(R.string.developer_email_adress)
            writeToSupportIntent.data = Uri.parse("mailto:")
            writeToSupportIntent.putExtra(Intent.EXTRA_EMAIL,arrayOf(mailAddress) )
            writeToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject )
            writeToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(writeToSupportIntent)
        }
        val buttonAgreement = findViewById<ImageView>(R.id.image_term)
        buttonAgreement.setOnClickListener {
            val link = getText(R.string.link_to_agreement)
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(link.toString())
            startActivity(agreementIntent)
        }
    }
}