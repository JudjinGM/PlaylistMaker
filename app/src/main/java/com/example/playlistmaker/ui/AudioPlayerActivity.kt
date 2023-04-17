package com.example.playlistmaker.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.ui.SearchActivity.Companion.TRACK
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {

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

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

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

        if (track.collectionName.isNotEmpty()) {
            albumTextView.text = track.collectionName
        } else albumTextView.visibility = View.GONE

        if (track.releaseDate.isNotEmpty()) {
            yearTextView.text = track.getShortReleaseDate()
        } else yearTextView.visibility = View.GONE

        if (track.primaryGenreName.isNotEmpty()) {
            genreTextView.text = track.primaryGenreName
        } else genreTextView.visibility = View.GONE

        if (track.country.isNotEmpty()) {
            countryTextView.text = track.country
        } else countryTextView.visibility = View.GONE

    }

    private fun onClicks() {
        backPlayerImageView.setOnClickListener {
            finish()
        }
    }
}