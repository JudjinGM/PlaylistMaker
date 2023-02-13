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
            val shareText = "https://practicum.yandex.ru/android-developer/"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(shareIntent, "Share" ))
        }
        val buttonSupport = findViewById<ImageView>(R.id.image_support)
        buttonSupport.setOnClickListener {
            val title = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
            writeToSupportIntent.data = Uri.parse("mailto:")
            writeToSupportIntent.putExtra(Intent.EXTRA_EMAIL,arrayOf("v4lievil@yandex.ru") )
            writeToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, title )
            writeToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(writeToSupportIntent)
        }
        val buttonAgreement = findViewById<ImageView>(R.id.image_term)
        buttonAgreement.setOnClickListener {
            val link = "https://yandex.ru/legal/practicum_offer/"
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(link)
            startActivity(agreementIntent)
        }
    }
}