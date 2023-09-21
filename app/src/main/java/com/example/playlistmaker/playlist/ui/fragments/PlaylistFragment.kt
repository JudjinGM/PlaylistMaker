package com.example.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.playlist.ui.model.PlaylistState
import com.example.playlistmaker.playlist.ui.model.SharePlaylistState
import com.example.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TracksAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.properties.Delegates

class PlaylistFragment : Fragment() {
    private var playlistId by Delegates.notNull<Long>()
    private val args: PlaylistFragmentArgs by navArgs()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistViewModel by viewModel {
        parametersOf(args.playlistId)
    }

    private lateinit var bottomSheetBehaviorTracks: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>


    private var tracksAdapter: TracksAdapter? = null

    private var confirmDialog: MaterialAlertDialogBuilder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistId = args.playlistId
        Log.d("judjin", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("judjin", "onCreateView")

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("judjin", "onViewCreated")

        initBottomSheetsTracks()
        initBottomSheetsMenu()
        initRecycleView()
        onClicksInit()

        confirmDialog = MaterialAlertDialogBuilder(
            requireContext(), R.style.CustomDialogTheme
        )

        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            renderPlaylistState(it)
        }

        viewModel.observeSharePlaylistState().observe(viewLifecycleOwner) {
            renderSharePlaylistState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("judjin", "onResume")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.bottomSheetTracks.playlistsRecycleView.adapter = null
        tracksAdapter = null
        _binding = null
        Log.d("judjin", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun initBottomSheetsTracks() {
        var bottomSheetPeekHeight = 0

        val viewTreeObserver: ViewTreeObserver = binding.supportingViewTrack.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (binding.supportingViewTrack.width > 0 && binding.supportingViewTrack.height > 0) {
                        bottomSheetPeekHeight = binding.supportingViewTrack.height
                        bottomSheetBehaviorTracks.peekHeight = bottomSheetPeekHeight
                        if (viewTreeObserver.isAlive) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    }
                }
            })
        }

        val bottomSheetContainer = binding.bottomSheetTracks.bottomSheetTracksLinearLayout
        bottomSheetBehaviorTracks = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehaviorTracks.peekHeight = bottomSheetPeekHeight

        bottomSheetBehaviorTracks.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    private fun initBottomSheetsMenu() {

        val bottomSheetContainer = binding.bottomSheetMenu.bottomSheetMenuLinearLayout
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        binding.bottomSheetTracks.bottomSheetTracksLinearLayout.isEnabled = true
                        binding.fragmentPlaylistConstraintLayout.isEnabled = true
                    }

                    else -> {
                        binding.overlay.isVisible = true
                        binding.bottomSheetTracks.bottomSheetTracksLinearLayout.isEnabled = false
                        binding.fragmentPlaylistConstraintLayout.isEnabled = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        binding.bottomSheetMenu.playlistView.playlistNameTextView
    }


    private fun initRecycleView() {
        tracksAdapter = TracksAdapter()
        binding.bottomSheetTracks.playlistsRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.bottomSheetTracks.playlistsRecycleView.adapter = tracksAdapter
    }

    private fun onClicksInit() {
        binding.backImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        tracksAdapter?.onTrackClicked = {
            val directions =
                PlaylistFragmentDirections.actionPlaylistFragmentToAudioPlayerFragment(it)
            findNavController().navigate(directions)
        }

        tracksAdapter?.onTrackClickedLong = {
            deleteTrackConfirmDialogRequest(it)
        }

        binding.shareImageView.setOnClickListener {
            viewModel.sharePlaylistClicked()
        }

        binding.moreImageView.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.bottomSheetMenu.shareTextView.setOnClickListener {
            viewModel.sharePlaylistClicked()
        }
        binding.bottomSheetMenu.editInfoTextView.setOnClickListener {

        }
        binding.bottomSheetMenu.deletePlaylistTextView.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlaylistConfirmDialogRequest()
        }

        binding.bottomSheetMenu.editInfoTextView.setOnClickListener {
            val directions =
                PlaylistFragmentDirections.actionPlaylistFragmentToEditPlaylistFragment(playlistId)
            findNavController().navigate(
                directions
            )
        }
    }

    private fun renderPlaylistState(state: PlaylistState) {
        when (state) {
            is PlaylistState.Success -> {
                if (state.playlistModel.playlistName.isNotEmpty()) {

                    val minutesCount = getTotalTracksTime(state.playlistModel.tracks)
                    val tracksCount = state.playlistModel.tracks.size

                    Glide.with(this).load(state.playlistModel.playlistCoverImage)
                        .placeholder(R.drawable.album).centerCrop()
                        .into(binding.playlistCoverPlayerImageView)
                    binding.playlistNameTextView.text = state.playlistModel.playlistName
                    binding.playlistDescriptionTextView.text =
                        state.playlistModel.playlistDescription
                    binding.tracksTimeTextView.text = resources.getQuantityString(
                        R.plurals.minute_plural, minutesCount, minutesCount
                    )
                    binding.tracksCountTextView.text = resources.getQuantityString(
                        R.plurals.tracks_plural, tracksCount, tracksCount
                    )

                    //init menu playlist view
                    Glide.with(this).load(state.playlistModel.playlistCoverImage)
                        .placeholder(R.drawable.no_album).centerCrop()
                        .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)))
                        .into(binding.bottomSheetMenu.playlistView.playlistCoverImageView)

                    binding.bottomSheetMenu.playlistView.playlistNameTextView.text =
                        state.playlistModel.playlistName
                    binding.bottomSheetMenu.playlistView.trackCountTextView.text =
                        resources.getQuantityString(
                            R.plurals.tracks_plural, tracksCount, tracksCount
                        )

                    //update recycle view
                    tracksAdapter?.updateAdapter(state.playlistModel.tracks)
                }

            }

            PlaylistState.CloseScreen -> findNavController().popBackStack()
        }
    }

    private fun renderSharePlaylistState(state: SharePlaylistState) {
        when (state) {
            SharePlaylistState.Error -> showToast(getString(R.string.share_playlist_error))
            SharePlaylistState.Success -> viewModel.sharePlaylist()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun deleteTrackConfirmDialogRequest(it: Track) {
        confirmDialog?.setTitle(R.string.delete_track_title)?.setMessage(R.string.delete_track)
            ?.setNegativeButton(R.string.cancel) { _, _ ->
            }?.setPositiveButton(R.string.yes_delete) { _, _ ->
                viewModel.deleteTrack(it)
            }
        confirmDialog?.show()
    }

    private fun deletePlaylistConfirmDialogRequest() {

        confirmDialog?.setTitle(R.string.delete_playlist_title)
            ?.setMessage(resources.getString(R.string.delete_playlist, viewModel.getPlaylistName()))
            ?.setNegativeButton(R.string.no) { _, _ ->
            }?.setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deletePlaylist()
            }
        confirmDialog?.show()
    }

    private fun getTotalTracksTime(tracks: List<Track>): Int {
        var durationSum = 0L
        tracks.forEach {
            durationSum += it.trackTimeMillis
        }
        return SimpleDateFormat("mm", Locale.getDefault()).format(durationSum).toInt()
    }
}