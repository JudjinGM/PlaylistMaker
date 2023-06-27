package com.example.playlistmaker.audio_player.ui.activity

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.audio_player.ui.model.PlayerError
import com.example.playlistmaker.audio_player.ui.model.PlayerState
import com.example.playlistmaker.audio_player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.activity.SearchActivity.Companion.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityAudioplayerBinding
    private var isTrackLiked = false

    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getTrack()

        viewContentInit()
        onClicks()

        viewModel.observePlayerState().observe(this) {
            renderPlayerState(it)
        }
        viewModel.observeTime().observe(this) {
            renderTimeState(it)
        }
        viewModel.observeToastState().observe(this) {
            renderToastErrorState(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun getTrack(): Track {
        val track = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java) ?: Track()
        } else intent.getParcelableExtra(TRACK) ?: Track()
        //if in intent somehow has no track, I set default empty track
        return track
    }

    private fun viewContentInit() {
        Glide.with(this).load(track.getCoverArtwork()).placeholder(R.drawable.album).centerInside()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.album_cover_player_corner_radius)))
            .into(binding.albumCoverPlayerImageView)

        binding.songNamePlayerTextView.text = track.trackName
        binding.artistNamePlayerTextView.text = track.artistName
        binding.durationTextView.text = track.trackTimeMillis
        binding.albumTextView.text = track.collectionName
        binding.yearTextView.text = track.getShortReleaseDate()
        binding.genreTextView.text = track.primaryGenreName
        binding.countryTextView.text = track.country
    }

    private fun onClicks() {
        binding.backPlayerImageView.setOnClickListener {
            finish()
        }
        binding.playImageView.setOnClickListener {
            viewModel.togglePlay()
        }

        binding.likeImageView.setOnClickListener {
            isTrackLiked = if (!isTrackLiked) {
                binding.likeImageView.setImageResource(R.drawable.like_button_like)
                true
            } else {
                binding.likeImageView.setImageResource(R.drawable.like_button_no_like)
                false
            }
        }
    }

    private fun renderPlayerState(playerState: PlayerState) {
        when (playerState) {
            is PlayerState.Error -> {
                binding.playImageView.setImageResource(R.drawable.play_button)
            }
            is PlayerState.Pause, PlayerState.Ready -> binding.playImageView.setImageResource(R.drawable.play_button)
            is PlayerState.Play -> binding.playImageView.setImageResource(R.drawable.pause_button)
        }
    }

    private fun renderTimeState(time: Long) {
        binding.timeTextView.text = millisToTimeFormat(time)
    }

    private fun renderToastErrorState(playerError: PlayerError) {
        when (playerError) {
            PlayerError.NOT_READY -> showToast(getString(R.string.player_not_ready))
            PlayerError.ERROR_OCCURRED -> showToast(getString(R.string.cant_play_song))
            PlayerError.NO_CONNECTION -> showToast(getString(R.string.error_network))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun millisToTimeFormat(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }
}