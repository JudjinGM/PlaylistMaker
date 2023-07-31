package com.example.playlistmaker.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        val navFragmentHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        val navController = navFragmentHost.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.mainBottomNavigation)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioPlayerFragment -> bottomNavigationView.isVisible = false
                else -> bottomNavigationView.isVisible = true
            }
        }
    }
}