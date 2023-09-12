package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.PlaylistAdapter
import com.example.playlistmaker.library.ui.model.PlaylistsError
import com.example.playlistmaker.library.ui.model.PlaylistsState
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistViewModel by viewModel()

    private var playlistAdapter: PlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleViewInit()
        onClicksInit()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun onClicksInit() {
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_createPlaylistFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.playlistRecycleView.adapter = null
        playlistAdapter = null
        _binding = null

    }

    private fun render(state: PlaylistsState) {
        when (state) {
            PlaylistsState.Loading -> showLoading()
            is PlaylistsState.Success.PlaylistContent -> showContent(state.playlists)
            is PlaylistsState.Error -> showError(state.error)
        }
    }

    private fun showContent(playlists: List<PlaylistModel>) {
        binding.progressBar.isVisible = false
        binding.playlistRecycleView.isVisible = true
        binding.errorPlaylistTextTextView.isVisible = false
        binding.placeholderLibraryPlaylistImage.isVisible = false

        playlistAdapter?.updateAdapter(playlists)
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.playlistRecycleView.isVisible = false
        binding.errorPlaylistTextTextView.isVisible = false
        binding.placeholderLibraryPlaylistImage.isVisible = false
    }

    private fun showError(error: PlaylistsError) {
        when (error) {
            PlaylistsError.EMPTY -> {
                binding.progressBar.isVisible = false
                binding.playlistRecycleView.isVisible = false
                binding.placeholderLibraryPlaylistImage.isVisible = true
                binding.errorPlaylistTextTextView.isVisible = true
                binding.errorPlaylistTextTextView.text = getString(R.string.error_no_playlist)
            }
        }
    }

    private fun recycleViewInit() {
        binding.playlistRecycleView.layoutManager =
            GridLayoutManager(requireContext(), RECYCLE_VIEW_GRID_COLUMNS_SIZE)
        playlistAdapter = PlaylistAdapter()
        binding.playlistRecycleView.adapter = playlistAdapter
    }

    companion object {
        const val RECYCLE_VIEW_GRID_COLUMNS_SIZE = 2
        fun newInstance() = PlaylistsFragment()
    }
}