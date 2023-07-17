package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.model.PlaylistsError
import com.example.playlistmaker.library.ui.model.PlaylistsState
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistViewModel by viewModel()

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
        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }
    }

    private fun render(state: PlaylistsState){
        when(state){
            is PlaylistsState.Error -> showError(state.error)
        }
    }

    private fun showError(error: PlaylistsError){
        when(error){
            PlaylistsError.EMPTY -> {
                binding.placeholderLibraryPlaylistImage.isVisible = true
                binding.errorPlaylistTextTextView.isVisible = true
                binding.errorPlaylistTextTextView.text = getString(R.string.error_no_playlist)
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}