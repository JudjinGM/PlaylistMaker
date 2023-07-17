package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.library.ui.model.FavoritesError
import com.example.playlistmaker.library.ui.model.FavoritesState
import com.example.playlistmaker.library.ui.view_model.FavoritesViewModel


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()

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
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Error -> showError(state.error)
        }
    }

    private fun showError(error: FavoritesError) {
        when (error) {
            FavoritesError.EMPTY -> {
                binding.placeholderLibraryFavoritesImage.isVisible = true
                binding.errorFavoritesTextTextView.isVisible = true
                binding.errorFavoritesTextTextView.text = getString(R.string.error_favorites_is_empty)
            }
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}
