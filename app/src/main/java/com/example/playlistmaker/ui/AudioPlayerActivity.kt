package com.example.playlistmaker.ui

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.MediaPlayerImplementation
import com.example.playlistmaker.domain.model.PlayerStatus
import com.example.playlistmaker.domain.model.PlayerStatus.*
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.usecases.MediaPlayerControlUseCase
import com.example.playlistmaker.domain.usecases.MediaPlayerInitUseCaseImpl
import com.example.playlistmaker.domain.usecases.MediaPlayerPlaybackControlUseCase
import com.example.playlistmaker.presenter.AudioPlayerView
import com.example.playlistmaker.ui.SearchActivity.Companion.TRACK
import java.text.SimpleDateFormat
import java.util.*

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

    private val mediaPlayer = MediaPlayerImplementation()

    private var playerState = STATE_DEFAULT

    private lateinit var urlForMusicPreview: String
    private lateinit var mainTreadHandler: Handler

    private var currentPlayingPosition = 0L

    private var isTrackLiked = false

    private val mediaPlayerInitUseCase = MediaPlayerInitUseCaseImpl(mediaPlayer) { playerStatus ->
        playerState = playerStatus
    }
    private val mediaPlayerControlUseCase = MediaPlayerControlUseCase(mediaPlayer) { playerStatus ->
        playerState = playerStatus
    }

    private val mediaPlayerPlaybackControlUseCase =
        MediaPlayerPlaybackControlUseCase(mediaPlayerControlUseCase) { showError() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        mainTreadHandler = Handler(Looper.getMainLooper())

        track = getTrack()
        viewInit()
        viewsContentInit()
        onClicks()

        mediaPlayerInitUseCase.initPlayer(urlForMusicPreview)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerControlUseCase.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
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
        urlForMusicPreview = track.previewUrl

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
            mediaPlayerPlaybackControlUseCase.execute(playerState)
            playerStatusUIUpdate(playerState)
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

    private fun playerStatusUIUpdate(playerStatus: PlayerStatus) {
        when (playerStatus) {
            STATE_DEFAULT, STATE_PREPARED, STATE_ERROR -> {
                playImageView.setImageResource(R.drawable.play_button)
                updateTimeTextView()
            }
            STATE_PLAYING -> {
                playImageView.setImageResource(R.drawable.pause_button)
                updateTimeTextView()
            }
            STATE_PAUSED -> {
                playImageView.setImageResource(R.drawable.play_button)
                updateTimeTextView()
            }
        }
    }

    private fun updateTimeTextView() {
        val newRunnable = object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_DEFAULT, STATE_PREPARED, STATE_ERROR -> {
                        mainTreadHandler.removeCallbacks(this)
                        currentPlayingPosition = 0L
                        timeTextView.text = millisToTimeFormat(currentPlayingPosition)
                    }
                    STATE_PLAYING -> {
                        currentPlayingPosition = mediaPlayer.getCurrentPosition()
                        timeTextView.text = millisToTimeFormat(currentPlayingPosition)
                        mainTreadHandler.postDelayed(this, DELAY)
                    }
                    STATE_PAUSED -> {
                        mainTreadHandler.removeCallbacks(this)
                        currentPlayingPosition = mediaPlayer.getCurrentPosition()
                        timeTextView.text = millisToTimeFormat(currentPlayingPosition)
                    }
                }
            }
        }
        mainTreadHandler.post(newRunnable)
    }

    private fun millisToTimeFormat(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }


    private fun showError() {
        Toast.makeText(this, R.string.cant_play_song, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val DELAY = 300L
    }
}