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
import com.example.playlistmaker.audioPlayer.ui.model.AddPlaylistState
import com.example.playlistmaker.audioPlayer.ui.model.BottomSheetState
import com.example.playlistmaker.audioPlayer.ui.model.FavoriteState
import com.example.playlistmaker.audioPlayer.ui.model.PlayerError
import com.example.playlistmaker.audioPlayer.ui.model.PlayerState
import com.example.playlistmaker.audioPlayer.ui.model.PlaylistListState
import com.example.playlistmaker.audioPlayer.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment : Fragment() {

    private lateinit var track: Track
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
        track = args.track ?: Track()
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
        initBottomSheet()
        recycleViewInit()
        onClicks()

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            renderPlayerState(it)
        }

        viewModel.observeErrorToastState().observe(viewLifecycleOwner) {
            renderToastErrorState(it)
        }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            renderFavoriteState(it)
        }

        viewModel.observePlaylistToastState().observe(viewLifecycleOwner) {
            renderToastPlaylistState(it)
        }

        viewModel.observeBottomSheetState().observe(viewLifecycleOwner) {
            renderBottomSheetState(it)
        }

        viewModel.observePlaylistListState().observe(viewLifecycleOwner) {
            renderPlaylistListState(it)
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


    private fun initBottomSheet() {
        val bottomSheetContainer = binding.bottomSheetLayout.bottomsheetLinearLayout
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

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun recycleViewInit() {
        playlistAdapter = PlaylistSmallAdapter()
        binding.bottomSheetLayout.playlistsRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.bottomSheetLayout.playlistsRecycleView.adapter = playlistAdapter
    }

    private fun onClicks() {
        binding.backImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playImageView.setOnClickListener {
            viewModel.togglePlay()
        }

        binding.likeImageView.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        playlistAdapter?.onPlaylistClicked = { playlist ->
            viewModel.addTrackToPlaylist(playlist)
        }

        binding.libraryImageView.setOnClickListener {
            viewModel.onLibraryClicked()
        }
        binding.bottomSheetLayout.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_createPlaylistFragment)
        }
    }

    private fun renderPlayerState(playerState: PlayerState) {
        if (playerState.isPlaying) {
            binding.playImageView.setImageResource(R.drawable.pause_button)
        } else binding.playImageView.setImageResource(R.drawable.play_button)

        binding.timeTextView.text = playerState.progress
    }

    private fun renderToastErrorState(playerError: PlayerError) {
        when (playerError) {
            PlayerError.NOT_READY -> showToast(getString(R.string.player_not_ready))
            PlayerError.ERROR_OCCURRED -> showToast(getString(R.string.cant_play_song))
            PlayerError.NO_CONNECTION -> showToast(getString(R.string.error_network))
        }
    }

    private fun renderToastPlaylistState(state: AddPlaylistState) {
        when (state) {
            is AddPlaylistState.Success -> showToast(getString(R.string.track_added_to_playlist))
            is AddPlaylistState.Error -> {
                when (state) {
                    is AddPlaylistState.Error.ErrorOccurred -> showToast(getString(R.string.error))
                    is AddPlaylistState.Error.AlreadyHaveTrack -> {
                        showToast(getString(R.string.track_already_in_playlist))
                    }
                }
            }
        }
    }

    private fun renderBottomSheetState(state: BottomSheetState) {
        when (state) {
            BottomSheetState.Hide -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            is BottomSheetState.Show -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun renderPlaylistListState(state: PlaylistListState) {
        when (state) {
            is PlaylistListState.Success -> {
                playlistAdapter?.updateAdapter(state.playlists)
            }
        }
    }

    private fun renderFavoriteState(state: FavoriteState) {
        when (state) {
            FavoriteState.Favorite -> binding.likeImageView.setImageResource(R.drawable.like_button_like)
            FavoriteState.NotFavorite -> binding.likeImageView.setImageResource(R.drawable.like_button_no_like)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}