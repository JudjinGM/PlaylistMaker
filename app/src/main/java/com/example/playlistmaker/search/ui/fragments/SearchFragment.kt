package com.example.playlistmaker.search.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TracksAdapter
import com.example.playlistmaker.search.ui.model.ErrorStatusUi
import com.example.playlistmaker.search.ui.model.PlaceholderStatus
import com.example.playlistmaker.search.ui.model.SearchState
import com.example.playlistmaker.search.ui.model.TextWatcherJustOnTextChanged
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()

    private var textWatcher: TextWatcher? = null
    private var tracksAdapter: TracksAdapter? = null

    private var inputMethodManager: InputMethodManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleViewInit()
        setOnClicksAndActions()

        viewModel.updateState()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }


    override fun onResume() {
        super.onResume()
        setOnTextWatchersTextChangeListeners()
        setClearInputTextImageViewVisibility(binding.inputSearchFieldEditText.text)
    }

    override fun onPause() {
        super.onPause()
        textWatcher?.let { binding.inputSearchFieldEditText.removeTextChangedListener(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tracksRecyclerView.adapter = null
        tracksAdapter = null
        _binding = null
    }


    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Error -> showError(state.errorStatus)
            is SearchState.Success -> {
                when (state) {
                    is SearchState.Success.SearchContent -> showSearchContent(state.tracks)
                    is SearchState.Success.ListenHistoryContent -> showListenHistory(state.tracks)
                    is SearchState.Success.Empty -> showEmpty()
                }
            }

            is SearchState.NavigateToPlayer -> navigateToPlayer(state.track)
        }
    }

    private fun showLoading() {
        tracksAdapter?.updateAdapter(listOf())
        showPlaceholder(PlaceholderStatus.PROGRESS_BAR)
        inputMethodManager?.hideSoftInputFromWindow(
            binding.inputSearchFieldEditText.windowToken, 0
        )
    }

    private fun showError(errorStatus: ErrorStatusUi) {
        tracksAdapter?.updateAdapter(listOf())
        when (errorStatus) {
            ErrorStatusUi.NOTHING_FOUND -> showPlaceholder(PlaceholderStatus.NOTHING_FOUND)
            ErrorStatusUi.NO_CONNECTION -> showPlaceholder(PlaceholderStatus.NO_CONNECTION)
        }
    }

    private fun showSearchContent(tracks: List<Track>) {
        tracksAdapter?.updateAdapter(tracks)
        showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    private fun showListenHistory(listenHistoryTracks: List<Track>) {
        tracksAdapter?.updateAdapter(listenHistoryTracks)
        showPlaceholder(PlaceholderStatus.LISTEN_HISTORY)
    }

    private fun showEmpty() {
        tracksAdapter?.updateAdapter(listOf())
        showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    private fun showPlaceholder(status: PlaceholderStatus) {
        when (status) {
            PlaceholderStatus.NO_PLACEHOLDER -> hideAll()

            PlaceholderStatus.NOTHING_FOUND -> with(binding) {
                hideAll()
                placeholderImage.visibility = View.VISIBLE
                errorTextTextView.visibility = View.VISIBLE
                errorTextTextView.text = getText(R.string.error_search)
                placeholderImage.setImageResource(R.drawable.error_search)
            }

            PlaceholderStatus.NO_CONNECTION -> with(binding) {
                hideAll()
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.error_internet)
                errorTextTextView.visibility = View.VISIBLE
                errorTextTextView.text = getText(R.string.error_internet)
                refreshButton.visibility = View.VISIBLE
            }

            PlaceholderStatus.LISTEN_HISTORY -> with(binding){
                hideAll()
                headerSearchHistoryTextView.visibility = View.VISIBLE
                buttonClearHistory.visibility = View.VISIBLE
            }

            PlaceholderStatus.PROGRESS_BAR -> {
                hideAll()
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun hideAll() {
        with(binding) {
            placeholderImage.visibility = View.GONE
            errorTextTextView.visibility = View.GONE
            refreshButton.visibility = View.GONE
            headerSearchHistoryTextView.visibility = View.GONE
            buttonClearHistory.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    private fun navigateToPlayer(track: Track) {
        val directions = SearchFragmentDirections.actionSearchFragmentToAudioPlayerFragment(track)
        findNavController().navigate(directions)
    }

    private fun recycleViewInit() {
        tracksAdapter = TracksAdapter()
        binding.tracksRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksRecyclerView.adapter = tracksAdapter
    }

    private fun setOnClicksAndActions() {
        inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.clearImageView.setOnClickListener {
            viewModel.clearSearchInput()

            inputMethodManager?.hideSoftInputFromWindow(
                binding.inputSearchFieldEditText.windowToken, 0
            )
            binding.inputSearchFieldEditText.setText(DEFAULT_TEXT)
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearListenHistory()
        }

        tracksAdapter?.onTrackClicked = { track ->
            viewModel.onTrackClicked(track)
        }

        binding.refreshButton.setOnClickListener {
            viewModel.refreshSearchDebounced()
        }
    }

    private fun setOnTextWatchersTextChangeListeners() {

        textWatcher = object : TextWatcherJustOnTextChanged {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setClearInputTextImageViewVisibility(s)

                if (binding.inputSearchFieldEditText.hasFocus() && (s?.isEmpty() == true || s?.isBlank() == true)) {
                    viewModel.clearSearchInput()
                }
                viewModel.searchDebounced(
                    changedText = s?.toString() ?: DEFAULT_TEXT
                )
            }
        }

        textWatcher?.let { binding.inputSearchFieldEditText.addTextChangedListener(it) }
    }

    private fun setClearInputTextImageViewVisibility(charSequence: CharSequence?) {
        binding.clearImageView.isVisible = checkImageViewVisibility(charSequence)
    }

    private fun checkImageViewVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    companion object {
        const val DEFAULT_TEXT = ""
    }
}