package com.example.playlistmaker.ui

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.PlayerStatus
import com.example.playlistmaker.domain.model.PlayerStatus.*
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presenter.AudioPlayerPresenter
import com.example.playlistmaker.presenter.AudioPlayerPresenterCreator
import com.example.playlistmaker.presenter.AudioPlayerView
import com.example.playlistmaker.ui.SearchActivity.Companion.TRACK
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity(), AudioPlayerView {

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

    private lateinit var mainTreadHandler: Handler
    private lateinit var trackTimeUpdateRunnable: Runnable
    private var isRunnablePosted = false

    private var isTrackLiked = false

    private lateinit var presenter: AudioPlayerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        mainTreadHandler = Handler(Looper.getMainLooper())

        track = getTrack()
        val audioPlayerPresenterCreator = AudioPlayerPresenterCreator(track)
        presenter = audioPlayerPresenterCreator.createPresenter(this)
        viewInit()
        viewsContentInit()
        onClicks()
    }

    override fun onPause() {
        super.onPause()
        presenter.pauseMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainTreadHandler.removeCallbacks(trackTimeUpdateRunnable)
        presenter.releaseMediaPlayer()
    }

    private fun getTrack(): Track {
        val track = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java) ?: Track()
        } else intent.getParcelableExtra(TRACK) ?: Track()
        //if in intent somehow has no track, I set default empty track
        return track
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

    private fun viewsContentInit() {
        Glide.with(this).load(track.getCoverArtwork()).placeholder(R.drawable.album).centerInside()
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
        playImageView.setOnClickListener {
            presenter.playClick()
        }

        likeImageView.setOnClickListener {
            isTrackLiked = if (!isTrackLiked) {
                likeImageView.setImageResource(R.drawable.like_button_like)
                true
            } else {
                likeImageView.setImageResource(R.drawable.like_button_no_like)
                false
            }
        }
    }

    override fun uiUpdate(playerStatus: PlayerStatus) {
        updateTimeTextView(playerStatus)
        Log.d("STATUS", "$playerStatus")
        when (playerStatus) {
            STATE_DEFAULT, STATE_PREPARED, STATE_ERROR -> {
                playImageView.setImageResource(R.drawable.play_button)
            }
            STATE_PLAYING -> {
                playImageView.setImageResource(R.drawable.pause_button)
            }
            STATE_PAUSED -> {
                playImageView.setImageResource(R.drawable.play_button)
            }
        }
    }

    private fun updateTimeTextView(playerStatus: PlayerStatus) {
        if (isRunnablePosted) {
            mainTreadHandler.removeCallbacks(trackTimeUpdateRunnable)
        }
        trackTimeUpdateRunnable = getTrackTimeUpdateRunnable(playerStatus)
        mainTreadHandler.post(trackTimeUpdateRunnable)
        isRunnablePosted = true
    }

    private fun getTrackTimeUpdateRunnable(playerStatus: PlayerStatus): Runnable {
        val result = object : Runnable {
            override fun run() {
                when (playerStatus) {
                    STATE_DEFAULT, STATE_PREPARED, STATE_ERROR -> {
                        mainTreadHandler.removeCallbacks(this)
                        timeTextView.text = millisToTimeFormat(0L)
                    }
                    STATE_PLAYING -> {
                        timeTextView.text = millisToTimeFormat(presenter.getTimeForUI())
                        mainTreadHandler.postDelayed(this, DELAY)
                    }
                    STATE_PAUSED -> {
                        timeTextView.text = millisToTimeFormat(presenter.getTimeForUI())
                    }
                }
            }
        }
        return result
    }

    private fun millisToTimeFormat(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    override fun showError() {
        Toast.makeText(this, R.string.cant_play_song, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val DELAY = 100L
    }
}