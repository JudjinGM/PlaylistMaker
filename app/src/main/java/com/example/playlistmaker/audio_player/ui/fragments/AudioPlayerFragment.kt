package com.example.playlistmaker.audio_player.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.audio_player.ui.model.PlayerError
import com.example.playlistmaker.audio_player.ui.model.PlayerState
import com.example.playlistmaker.audio_player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {
    private lateinit var track: Track
    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!

    private var isTrackLiked = false

    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(TRACK, Track::class.java) ?: Track()
        } else requireArguments().getParcelable(TRACK) ?: Track()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewContentInit()
        onClicks()

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            renderPlayerState(it)
        }
        viewModel.observeTime().observe(viewLifecycleOwner) {
            renderTimeState(it)
        }
        viewModel.observeToastState().observe(viewLifecycleOwner) {
            renderToastErrorState(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
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
            findNavController().popBackStack()
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
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun millisToTimeFormat(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    companion object {
        const val TRACK = "track"
    }
}