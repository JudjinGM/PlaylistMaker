package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.buttonSearch)
        val libraryButton = findViewById<Button>(R.id.buttonLibrary)
        val settingButton = findViewById<Button>(R.id.buttonSettings)

        searchButton.setOnClickListener {
            navigateTo(SearchActivity::class.java)
        }
        libraryButton.setOnClickListener {
            navigateTo(LibraryActivity::class.java)
        }
        settingButton.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
    }
    private fun navigateTo(clazz: Class<out AppCompatActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}
