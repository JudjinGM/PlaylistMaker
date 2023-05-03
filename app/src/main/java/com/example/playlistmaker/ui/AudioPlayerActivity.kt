package com.example.playlistmaker.ui

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.ui.SearchActivity.Companion.TRACK

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track

    private lateinit var backPlayerImageView: ImageView
    private lateinit var albumCoverPlayerImageView: ImageView
    private lateinit var songNameTextView: TextView
    private lateinit var artistNameTextView: TextView
    private lateinit var libraryImageView: ImageView
    private lateinit var playImageView: ImageView
    private lateinit var likeImageView: ImageView
    private lateinit var timeTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var albumTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var countryTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        track = if(VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(TRACK, Track::class.java) ?: Track()
        } else intent.getParcelableExtra(TRACK) ?: Track()

        viewInit()
        contentInit()
        onClicks()
    }

    private fun viewInit() {
        backPlayerImageView = findViewById(R.id.backPlayerImageView)
        albumCoverPlayerImageView = findViewById(R.id.albumCoverPlayerImageView)
        songNameTextView = findViewById(R.id.songNamePlayerTextView)
        artistNameTextView = findViewById(R.id.artistNamePlayerTextView)
        libraryImageView = findViewById(R.id.libraryImageView)
        playImageView = findViewById(R.id.playImageView)
        likeImageView = findViewById(R.id.likeImageView)
        timeTextView = findViewById(R.id.timeTextView)
        durationTextView = findViewById(R.id.durationTextView)
        albumTextView = findViewById(R.id.albumTextView)
        yearTextView = findViewById(R.id.yearTextView)
        genreTextView = findViewById(R.id.genreTextView)
        countryTextView = findViewById(R.id.countryTextView)
    }

    private fun contentInit() {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.album)
            .centerInside()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.album_cover_player_corner_radius)))
            .into(albumCoverPlayerImageView)

        songNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        durationTextView.text = track.trackTimeMillis
        albumTextView.text = track.collectionName
        yearTextView.text = track.getShortReleaseDate()
        genreTextView.text = track.primaryGenreName
        countryTextView.text = track.country
    }

    private fun onClicks() {
        backPlayerImageView.setOnClickListener {
            finish()
        }
    }
}