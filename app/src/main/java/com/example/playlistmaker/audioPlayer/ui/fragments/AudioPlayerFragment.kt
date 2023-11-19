package com.example.playlistmaker.audioPlayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.audioPlayer.ui.PlaylistSmallAdapter
import com.example.playlistmaker.audioPlayer.ui.model.AudioPlayerState
import com.example.playlistmaker.audioPlayer.ui.model.PlayerError
import com.example.playlistmaker.audioPlayer.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!

    private var playlistAdapter: PlaylistSmallAdapter? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val args: AudioPlayerFragmentArgs by navArgs()

    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(args.track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.initMediaPlayer()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initState()

        initBottomSheet()
        recycleViewInit()
        onClicks()

        viewModel.observeAudioPlayerState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playlistAdapter = null
        binding.bottomSheetLayout.playlistsRecycleView.adapter = null
        _binding = null
    }

    private fun initBottomSheet() {
        val bottomSheetContainer = binding.bottomSheetLayout.bottomSheetAddTracksLinearLayout
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        })
    }

    private fun recycleViewInit() {
        playlistAdapter = PlaylistSmallAdapter()
        binding.bottomSheetLayout.playlistsRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.bottomSheetLayout.playlistsRecycleView.adapter = playlistAdapter
    }

    private fun onClicks() {
        with(binding) {
            backImageView.setOnClickListener {
                findNavController().popBackStack()
            }

            playImageView.setOnClickListener {
                viewModel.togglePlay()
            }

            likeImageView.setOnClickListener {
                viewModel.onFavoriteClicked()
            }

            playlistAdapter?.onPlaylistClicked = { playlist ->
                viewModel.addTrackToPlaylist(playlist)
            }

            libraryImageView.setOnClickListener {
                viewModel.onLibraryClicked()
            }
            bottomSheetLayout.newPlaylistButton.setOnClickListener {
                findNavController().navigate(R.id.action_audioPlayerFragment_to_createPlaylistFragment)
            }
        }
    }

    private fun renderState(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.InitState -> {
                with(binding) {
                    Glide.with(this@AudioPlayerFragment)
                        .load(state.track.getCoverArtwork())
                        .placeholder(R.drawable.album)
                        .centerInside()
                        .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.album_cover_player_corner_radius)))
                        .into(albumCoverPlayerImageView)

                    songNamePlayerTextView.text = state.track.trackName
                    artistNamePlayerTextView.text = state.track.artistName
                    durationTextView.text = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(state.track.trackTimeMillis)
                    albumTextView.text = state.track.collectionName
                    yearTextView.text = state.track.getShortReleaseDate()
                    genreTextView.text = state.track.primaryGenreName
                    countryTextView.text = state.track.country
                    playlistAdapter?.updateAdapter(state.playlists)
                }
            }

            is AudioPlayerState.TrackState -> {
                if (state.isFavorite) {
                    binding.likeImageView.setImageResource(R.drawable.like_button_like)
                } else binding.likeImageView.setImageResource(R.drawable.like_button_no_like)
            }

            is AudioPlayerState.PlayerState -> with(binding) {
                when (state) {
                    is AudioPlayerState.PlayerState.Playing -> {
                        playImageView.setImageResource(R.drawable.pause_button)
                    }

                    AudioPlayerState.PlayerState.Default -> {
                        playImageView.setImageResource(R.drawable.play_button)
                    }

                    is AudioPlayerState.PlayerState.Error -> {
                        showToastPlayerError(state.error)
                        playImageView.setImageResource(R.drawable.play_button)
                    }

                    is AudioPlayerState.PlayerState.Paused -> {
                        playImageView.setImageResource(R.drawable.play_button)
                    }

                    AudioPlayerState.PlayerState.Prepared -> {
                        playImageView.setImageResource(R.drawable.play_button)
                    }
                }
                binding.timeTextView.text = state.progress
            }

            AudioPlayerState.PlayListState.Success -> showToast(getString(R.string.track_added_to_playlist))
            AudioPlayerState.PlayListState.Error.AlreadyHaveTrack -> showToast(getString(R.string.track_already_in_playlist))
            AudioPlayerState.PlayListState.Error.ErrorOccurred -> showToast(getString(R.string.error))
            AudioPlayerState.BottomSheetState.Hide -> bottomSheetBehavior.state =
                BottomSheetBehavior.STATE_HIDDEN

            AudioPlayerState.BottomSheetState.Show -> bottomSheetBehavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun showToastPlayerError(playerError: PlayerError) {
        when (playerError) {
            PlayerError.NOT_READY -> showToast(getString(R.string.player_not_ready))
            PlayerError.ERROR_OCCURRED -> showToast(getString(R.string.cant_play_song))
            PlayerError.NO_CONNECTION -> showToast(getString(R.string.error_network))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}