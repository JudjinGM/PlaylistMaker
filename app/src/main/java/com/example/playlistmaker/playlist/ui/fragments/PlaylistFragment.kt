package com.example.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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

    private var tracksAdapter: TracksAdapter? = null

    private var confirmDialog: MaterialAlertDialogBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistId = args.playlistId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomSheetTracks()
        initRecycleView()
        onClicksInit()

        confirmDialog = MaterialAlertDialogBuilder(
            requireContext(), R.style.MaterialAlertDialogText
        )

        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            renderPlaylistState(it)
        }

        viewModel.observeSharePlaylistState().observe(viewLifecycleOwner) {
            renderSharePlaylistState(it)
        }
    }

    private fun initBottomSheetTracks() {
        val peekHeight = binding.supportingView.measuredHeight
        val bottomSheetContainer = binding.bottomSheetTracks.bottomsheetLinearLayout
        bottomSheetBehaviorTracks = BottomSheetBehavior.from(bottomSheetContainer)
//        bottomSheetBehaviorTracks.peekHeight = peekHeight
        Log.d("judjin", "$peekHeight")

        bottomSheetBehaviorTracks.state = BottomSheetBehavior.STATE_COLLAPSED

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
    }

    private fun renderPlaylistState(state: PlaylistState) {
        when (state) {
            is PlaylistState.Loading -> {

            }

            is PlaylistState.Error -> {

            }

            is PlaylistState.Success -> {

                val minutesCount = getTotalTracksTime(state.playlistModel.tracks)
                val tracksCount = state.playlistModel.tracks.size

                Glide.with(this).load(state.playlistModel.playlistCoverImage)
                    .placeholder(R.drawable.album).centerInside()
                    .into(binding.playlistCoverPlayerImageView)
                binding.playlistNameTextView.text = state.playlistModel.playlistName
                binding.playlistDescriptionTextView.text = state.playlistModel.playlistDescription
                binding.tracksTimeTextView.text = resources.getQuantityString(
                    R.plurals.minute_plural, minutesCount, minutesCount
                )
                binding.tracksCountTextView.text = resources.getQuantityString(
                    R.plurals.tracks_plural, tracksCount, tracksCount
                )

                tracksAdapter?.updateAdapter(state.playlistModel.tracks)

            }
        }
    }

    private fun renderSharePlaylistState(state: SharePlaylistState) {
        when (state) {
            SharePlaylistState.Error -> showToast(getString(R.string.share_playlist_error))
            SharePlaylistState.Success -> viewModel.sharePlaylist()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun deleteTrackConfirmDialogRequest(it: Track) {

        confirmDialog?.setTitle(R.string.delete_track)
            ?.setNegativeButton(R.string.no) { _, _ ->
            }
            ?.setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteTrack(it)
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