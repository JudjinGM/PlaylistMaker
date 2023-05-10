package com.example.playlistmaker.ui

import android.media.MediaPlayer
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
import com.example.playlistmaker.data.model.PlayerStatus
import com.example.playlistmaker.data.model.Track
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

    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerStatus.STATE_DEFAULT
    private lateinit var urlForMusicPreview: String
    private lateinit var mainTreadHandler: Handler

    private var currentPlayingTimeMillis: Long = 0L // the current time elapsed since the pause
    private var elapsedTime: Long = 0L // the current time that passes while the music is playing
    private var isRunnablePosted = false // to check that only one Runnable is currently running
    private var isTrackLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        track = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java) ?: Track()
        } else intent.getParcelableExtra(TRACK) ?: Track()
        //if in intent somehow has no track, I set default empty track

        mainTreadHandler = Handler(Looper.getMainLooper())

        viewInit()
        contentInit()
        onClicks()
        preparePlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
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
            playbackControl()
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

    private fun preparePlayer() {
        try {
            mediaPlayer.setDataSource(urlForMusicPreview)
            mediaPlayer.prepareAsync() // source is url so Async then
            mediaPlayer.setOnPreparedListener {
                playerState = PlayerStatus.STATE_PREPARED
                playImageView.setImageResource(R.drawable.play_button)
                updateTimeTextView()
            }
        } catch (e: Exception) {
            showError()
        }
        // just in case if there is a problem with mediaPlayer set up exception may occur

        mediaPlayer.setOnCompletionListener {
            playerState = PlayerStatus.STATE_PREPARED
            playImageView.setImageResource(R.drawable.play_button)
            updateTimeTextView()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerStatus.STATE_PLAYING
        playImageView.setImageResource(R.drawable.pause_button)
        updateTimeTextView()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerStatus.STATE_PAUSED
        playImageView.setImageResource(R.drawable.play_button)
        updateTimeTextView()
    }

    private fun playbackControl() {
        when (playerState) {
            PlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }
            PlayerStatus.STATE_DEFAULT -> {
                showError()
            }
        }
    }

    private fun updateTimeTextView() {
        val startTime: Long = System.currentTimeMillis()
        var isRunnableExecute = false
        val newRunnable = object : Runnable {
            override fun run() {
                when (playerState) {
                    PlayerStatus.STATE_DEFAULT, PlayerStatus.STATE_PREPARED -> {
                        mainTreadHandler.removeCallbacks(this)
                        currentPlayingTimeMillis = 0L
                        elapsedTime = 0L
                        timeTextView.text = millisToTimeFormat(currentPlayingTimeMillis)
                    }
                    PlayerStatus.STATE_PLAYING -> {
                        elapsedTime =
                            System.currentTimeMillis() - startTime
                        timeTextView.text =
                            millisToTimeFormat(elapsedTime + currentPlayingTimeMillis)
                        mainTreadHandler.postDelayed(this, DELAY)
                        isRunnableExecute = true
                    }
                    PlayerStatus.STATE_PAUSED -> {
                        mainTreadHandler.removeCallbacks(this)
                        if (!isRunnableExecute) {
                            currentPlayingTimeMillis += elapsedTime
                            timeTextView.text = millisToTimeFormat(currentPlayingTimeMillis)
                        }
                    }
                }
            }
        }
        if (isRunnablePosted) { //checking if already Runnable posted so it's just one Runnable in a time
            mainTreadHandler.removeCallbacks(newRunnable)
        }
        mainTreadHandler.post(newRunnable)
        isRunnablePosted = true
    }

    private fun millisToTimeFormat(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    private fun showError() {
        Toast.makeText(this, R.string.cant_play_song, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val DELAY =
            10L // if delay set higher, when play button pressed quickly, the time may fails
    }
}