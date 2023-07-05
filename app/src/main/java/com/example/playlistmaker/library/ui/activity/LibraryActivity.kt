package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewInit()
        setOnClicks()
    }

    private fun viewInit() {
        binding.libraryViewPager.adapter =
            LibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabLayoutMediator = TabLayoutMediator(
            binding.libraryTableLayout, binding.libraryViewPager
        ) { tab, position ->
            tab.text = if (position == 0) getString(R.string.favorite_tracks)
            else getString(R.string.playlists)
        }
        tabLayoutMediator.attach()
    }

    private fun setOnClicks() {
        binding.backImageView.setOnClickListener {
            finish()
        }
    }
}