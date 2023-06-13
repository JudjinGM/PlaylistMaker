package com.example.playlistmaker.audio_player.ui.activity

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.audio_player.domain.model.PlayerError
import com.example.playlistmaker.audio_player.domain.model.PlayerState
import com.example.playlistmaker.audio_player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.activity.SearchActivity.Companion.TRACK
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track

    private lateinit var binding: ActivityAudioplayerBinding

    private var isTrackLiked = false

    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getTrack()
        viewModel = ViewModelProvider(
            this, AudioPlayerViewModel.getViewModelFactory(track)
        )[AudioPlayerViewModel::class.java]

        viewContentInit()
        onClicks()

        viewModel.observeState().observe(this) {
            renderPlayerState(it)
        }

        viewModel.observeTime().observe(this) {
            renderTimeState(it)
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
                showError(playerState.error)
            }
            is PlayerState.Pause -> binding.playImageView.setImageResource(R.drawable.play_button)
            is PlayerState.Play -> binding.playImageView.setImageResource(R.drawable.pause_button)
        }
    }

    private fun renderTimeState(time: Long) {
        binding.timeTextView.text = millisToTimeFormat(time)
    }

    private fun millisToTimeFormat(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    private fun showError(playerError: PlayerError) {
        if (playerError == PlayerError.ERROR_OCCURRED) {
            Toast.makeText(this, R.string.cant_play_song, Toast.LENGTH_LONG).show()
        }
        if (playerError == PlayerError.NOT_READY) {
            Toast.makeText(this, R.string.player_not_ready, Toast.LENGTH_LONG).show()

        }
    }

}