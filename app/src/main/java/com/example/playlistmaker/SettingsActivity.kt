package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
        val buttonTextShare = findViewById<TextView>(R.id.text_share_app)
        val shareClickListener = View.OnClickListener {
            val shareText = getText(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }

        buttonShare.setOnClickListener(shareClickListener)
        buttonTextShare.setOnClickListener(shareClickListener)

        val buttonSupport = findViewById<ImageView>(R.id.image_support)
        val buttonTextSupport = findViewById<TextView>(R.id.text_support)
        val supportClickListener = View.OnClickListener {
            val subject = getText(R.string.mail_to_support_subject)
            val message = getText(R.string.mail_to_support_message)
            val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
            val mailAddress = getText(R.string.developer_email_adress)
            writeToSupportIntent.data = Uri.parse("mailto:")
            writeToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailAddress))
            writeToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            writeToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(writeToSupportIntent)
        }

        buttonSupport.setOnClickListener(supportClickListener)
        buttonTextSupport.setOnClickListener(supportClickListener)

        val buttonAgreement = findViewById<ImageView>(R.id.image_term)
        val buttonTextAgreement = findViewById<TextView>(R.id.text_term)
        val agreementClickListener = View.OnClickListener {
            val link = getText(R.string.link_to_agreement)
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(link.toString())
            startActivity(agreementIntent)
        }

        buttonAgreement.setOnClickListener(agreementClickListener)
        buttonTextAgreement.setOnClickListener(agreementClickListener)
    }
}