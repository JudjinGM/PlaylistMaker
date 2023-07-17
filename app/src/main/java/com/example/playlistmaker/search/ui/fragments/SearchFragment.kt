package com.example.playlistmaker.search.ui.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.audio_player.ui.fragments.AudioPlayerFragment.Companion.TRACK
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.model.ErrorStatus
import com.example.playlistmaker.search.domain.model.PlaceholderStatus
import com.example.playlistmaker.search.domain.model.SearchState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TracksAdapter
import com.example.playlistmaker.search.ui.model.SavedTracks
import com.example.playlistmaker.search.ui.model.TextWatcherJustOnTextChanged
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var tracksAdapter: TracksAdapter
    private var savedSearchedTracks: SavedTracks? = null

    //for restoring tracks from bundle if android kills app
    //private var savedSearchedTracks = SavedTracks(tracks = null)


    private var inputSearchText: String = DEFAULT_TEXT

    private val viewModel: SearchViewModel by viewModel()
    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true
    private var isFragmentJustCreated = false

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFragmentJustCreated = true

        savedSearchedTracks = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState?.getParcelable(SAVED_SEARCH_TRACKS, SavedTracks::class.java)
        } else savedInstanceState?.getParcelable(SAVED_SEARCH_TRACKS)

        Log.d("judjin", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("judjin", "onCreateView")

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.init(savedSearchedTracks)

        viewModel.observeSavedTracks().observe(viewLifecycleOwner) {tracks->
            savedSearchedTracks = SavedTracks(ArrayList(tracks))
        }


        recycleViewInit()
        setOnClicksAndActions()
        Log.d("judjin", "onViewCreated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            inputSearchText = savedInstanceState.getString(SAVED_TEXT, DEFAULT_TEXT)
        }
        binding.inputSearchFieldEditText.setText(inputSearchText)
        Log.d("judjin", "onViewStateRestored")
    }

    override fun onStart() {
        super.onStart()
        Log.d("judjin", "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("judjin", "onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d("judjin", "onPause")
    }

    override fun onResume() {
        super.onResume()
        if (!isFragmentJustCreated) {
            viewModel.updateState()
        }
        isFragmentJustCreated = false
        setOnTextWatchersTextChangeListeners()
        setClearInputTextImageViewVisibility(inputSearchText)

        Log.d("judjin", "onResume")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
        outState.putParcelable(SAVED_SEARCH_TRACKS, savedSearchedTracks)
        Log.d("judjin", "onSaveInstanceState")
    }

    override fun onDestroy() {
        Log.d("judjin", "onDestroy")
        textWatcher?.let { binding.inputSearchFieldEditText.removeTextChangedListener(it) }
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("judjin", "onDestroyView")

        super.onDestroyView()
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
        }
    }

    private fun showLoading() {
        tracksAdapter.updateAdapter(listOf())
        showPlaceholder(PlaceholderStatus.PROGRESS_BAR)
    }

    private fun showError(errorStatus: ErrorStatus) {
        tracksAdapter.updateAdapter(listOf())
        when (errorStatus) {
            ErrorStatus.NOTHING_FOUND -> showPlaceholder(PlaceholderStatus.NOTHING_FOUND)
            ErrorStatus.NO_CONNECTION -> showPlaceholder(PlaceholderStatus.NO_CONNECTION)
        }
    }

    private fun showSearchContent(tracks: List<Track>) {
        tracksAdapter.updateAdapter(tracks)
        showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    private fun showListenHistory(listenHistoryTracks: List<Track>) {
        tracksAdapter.updateAdapter(listenHistoryTracks)
        showPlaceholder(PlaceholderStatus.LISTEN_HISTORY)
    }

    private fun showEmpty() {
        tracksAdapter.updateAdapter(listOf())
        showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    private fun showPlaceholder(status: PlaceholderStatus) {
        when (status) {
            PlaceholderStatus.NO_PLACEHOLDER -> {
                binding.placeholderImage.visibility = View.GONE
                binding.errorTextTextView.visibility = View.GONE
                binding.refreshButton.visibility = View.GONE
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            PlaceholderStatus.NOTHING_FOUND -> {
                binding.refreshButton.visibility = View.GONE
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.error_search)
                binding.errorTextTextView.visibility = View.VISIBLE
                binding.errorTextTextView.text = getText(R.string.error_search)
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            PlaceholderStatus.NO_CONNECTION -> {
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.error_internet)
                binding.errorTextTextView.visibility = View.VISIBLE
                binding.errorTextTextView.text = getText(R.string.error_internet)
                binding.refreshButton.visibility = View.VISIBLE
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            PlaceholderStatus.LISTEN_HISTORY -> {
                binding.placeholderImage.visibility = View.GONE
                binding.errorTextTextView.visibility = View.GONE
                binding.refreshButton.visibility = View.GONE
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.headerSearchHistoryTextView.visibility = View.VISIBLE
                binding.buttonClearHistory.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

            PlaceholderStatus.PROGRESS_BAR -> {
                binding.placeholderImage.visibility = View.GONE
                binding.errorTextTextView.visibility = View.GONE
                binding.refreshButton.visibility = View.GONE
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun recycleViewInit() {
        tracksAdapter = TracksAdapter()
        binding.tracksRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksRecyclerView.adapter = tracksAdapter
    }

    private fun setOnClicksAndActions() {

        binding.clearImageView.setOnClickListener {
            viewModel.clearSearchInput()


            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.inputSearchFieldEditText.windowToken, 0
            )
            binding.inputSearchFieldEditText.setText(DEFAULT_TEXT)
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearListenHistory()
        }

        tracksAdapter.onTrackClicked = { track ->
            if (clickDebounce()) {
                viewModel.addToListenHistory(track)
                findNavController().navigate(
                    R.id.action_searchFragment_to_audioPlayerFragment,
                    createArgs(track)
                )
            }
        }

        binding.refreshButton.setOnClickListener {
            if (clickDebounce()) {
                viewModel.searchDebounced(inputSearchText)
            }
        }
    }

    private fun setOnTextWatchersTextChangeListeners() {

        textWatcher = object : TextWatcherJustOnTextChanged {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setClearInputTextImageViewVisibility(s)
                inputSearchText = binding.inputSearchFieldEditText.text.toString()

                if (binding.inputSearchFieldEditText.hasFocus() && (s?.isEmpty() == true || s?.isBlank() == true)) {
                    viewModel.clearSearchInput()
                }
                viewModel.searchDebounced(
                    changedText = s?.toString() ?: ""
                )
            }
        }
        textWatcher?.let { binding.inputSearchFieldEditText.addTextChangedListener(it) }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    private fun setClearInputTextImageViewVisibility(charSequence: CharSequence?) {
        binding.clearImageView.isVisible = checkImageViewVisibility(charSequence)
    }

    private fun checkImageViewVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val SAVED_SEARCH_TRACKS = "SAVED_SEARCH_TRACKS"
        const val DEFAULT_TEXT = ""
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L

        fun createArgs(track: Track) =
            bundleOf(TRACK to track)
    }
}