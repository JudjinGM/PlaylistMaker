package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.library.ui.model.FavoritesError
import com.example.playlistmaker.library.ui.model.FavoritesState
import com.example.playlistmaker.library.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TracksAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private var tracksAdapter: TracksAdapter? = null

    private var isClickAllowed = true

    private val viewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isClickAllowed = true

        recycleViewInit()

        onClicksInit()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tracksRecyclerView.adapter = null
        tracksAdapter = null
        _binding = null
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Error -> showError(state.error)
            FavoritesState.Loading -> showLoading()
            is FavoritesState.Success.FavoriteContent -> showContent(state.tracks)
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.placeholderLibraryFavoritesImage.isVisible = false
        binding.errorFavoritesTextTextView.isVisible = false
    }

    private fun showContent(tracks: List<Track>) {
        binding.progressBar.isVisible = false
        binding.placeholderLibraryFavoritesImage.isVisible = false
        binding.errorFavoritesTextTextView.isVisible = false

        tracksAdapter?.updateAdapter(tracks)
    }

    private fun showError(error: FavoritesError) {
        binding.progressBar.isVisible = false
        binding.placeholderLibraryFavoritesImage.isVisible = true
        binding.errorFavoritesTextTextView.isVisible = true
        when (error) {
            FavoritesError.EMPTY -> {
                binding.errorFavoritesTextTextView.text =
                    getString(R.string.error_favorites_is_empty)
            }
        }
    }

    private fun isClickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun recycleViewInit() {
        tracksAdapter = TracksAdapter()
        binding.tracksRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tracksRecyclerView.adapter = tracksAdapter
    }

    private fun onClicksInit() {

        tracksAdapter?.onTrackClicked = { track ->
            if (isClickDebounce()) {
                viewModel.addToListenHistory(track)
                val directions =
                    LibraryFragmentDirections.actionLibraryFragmentToAudioPlayerFragment(track)
                findNavController().navigate(directions)
            }
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}
