package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.audio_player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.model.ErrorStatus
import com.example.playlistmaker.search.domain.model.PlaceholderStatus
import com.example.playlistmaker.search.domain.model.PlaceholderStatus.*
import com.example.playlistmaker.search.domain.model.SearchState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.model.SavedTracks
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var tracksAdapter: TracksAdapter

    //for restoring tracks from bundle if android kills app
    private var savedSearchedTracks = SavedTracks(tracks = null)
    private var inputSearchText: String = DEFAULT_TEXT

    private val viewModel: SearchViewModel by viewModel<SearchViewModel>()
    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true
    private var isActivityJustCreated = false

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedTracksFromBundle: SavedTracks? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState?.getParcelable(SAVED_SEARCH_TRACKS, SavedTracks::class.java)
            } else savedInstanceState?.getParcelable(SAVED_SEARCH_TRACKS)

        isActivityJustCreated = true

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeSavedTracks().observe(this) {
            savedSearchedTracks = SavedTracks(ArrayList(it))
        }

        viewModel.init(savedTracksFromBundle)

        recycleViewInit()
        setOnClicksAndActions()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SAVED_TEXT, DEFAULT_TEXT)
        binding.inputSearchFieldEditText.setText(inputSearchText)
    }

    override fun onResume() {
        super.onResume()
        if (isActivityJustCreated) {
            setClearInputTextImageViewVisibility(inputSearchText)
            setOnTextWatchersTextChangeListeners()  // moved here to avoid searchDebounce after restore
        } else {
            viewModel.updateState() // to update screen when returning from AudioPlayer Activity
        }
        isActivityJustCreated = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
        outState.putParcelable(SAVED_SEARCH_TRACKS, savedSearchedTracks)
    }

    override fun onDestroy() {
        textWatcher?.let { binding.inputSearchFieldEditText.removeTextChangedListener(it) }
        super.onDestroy()
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Error -> showError(state.errorStatus)
            is SearchState.Success -> {
                when (state) {
                    is SearchState.Success.SearchContent -> showSearchContent(state.tracks)
                    is SearchState.Success.ListenHistoryContent -> showListenHistory(state.tracks)
                    SearchState.Success.Empty -> showEmpty()
                }
            }
        }
    }

    private fun showLoading() {
        tracksAdapter.updateAdapter(listOf())
        showPlaceholder(PROGRESS_BAR)
    }

    private fun showError(errorStatus: ErrorStatus) {
        tracksAdapter.updateAdapter(listOf())
        when (errorStatus) {
            ErrorStatus.NOTHING_FOUND -> showPlaceholder(NOTHING_FOUND)
            ErrorStatus.NO_CONNECTION -> showPlaceholder(NO_CONNECTION)
        }
    }

    private fun showSearchContent(tracks: List<Track>) {
        tracksAdapter.updateAdapter(tracks)
        showPlaceholder(NO_PLACEHOLDER)
    }

    private fun showListenHistory(listenHistoryTracks: List<Track>) {
        tracksAdapter.updateAdapter(listenHistoryTracks)
        showPlaceholder(LISTEN_HISTORY)
    }

    private fun showEmpty() {
        tracksAdapter.updateAdapter(listOf())
        showPlaceholder(NO_PLACEHOLDER)
    }

    private fun showPlaceholder(status: PlaceholderStatus) {
        when (status) {
            NO_PLACEHOLDER -> {
                binding.placeholderImage.visibility = View.GONE
                binding.errorTextTextView.visibility = View.GONE
                binding.refreshButton.visibility = View.GONE
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
            NOTHING_FOUND -> {
                binding.refreshButton.visibility = View.GONE
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.error_search)
                binding.errorTextTextView.visibility = View.VISIBLE
                binding.errorTextTextView.text = getText(R.string.error_search)
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
            NO_CONNECTION -> {
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.error_internet)
                binding.errorTextTextView.visibility = View.VISIBLE
                binding.errorTextTextView.text = getText(R.string.error_internet)
                binding.refreshButton.visibility = View.VISIBLE
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
            LISTEN_HISTORY -> {
                binding.placeholderImage.visibility = View.GONE
                binding.errorTextTextView.visibility = View.GONE
                binding.refreshButton.visibility = View.GONE
                binding.headerSearchHistoryTextView.visibility = View.GONE
                binding.buttonClearHistory.visibility = View.GONE
                binding.headerSearchHistoryTextView.visibility = View.VISIBLE
                binding.buttonClearHistory.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
            PROGRESS_BAR -> {
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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.tracksRecyclerView.adapter = tracksAdapter
    }

    private fun setOnClicksAndActions() {
        binding.backImageView.setOnClickListener {
            finish()
        }

        binding.clearImageView.setOnClickListener {
            viewModel.clearSearchInput()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra(TRACK, track)
                startActivity(intent)
            }
        }

        binding.refreshButton.setOnClickListener {
            if (clickDebounce()) {
                viewModel.searchDebounced(inputSearchText)
            }
        }
    }

    private fun setClearInputTextImageViewVisibility(charSequence: CharSequence?) {
        binding.clearImageView.isVisible = checkImageViewVisibility(charSequence)
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

    private fun checkImageViewVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val SAVED_SEARCH_TRACKS = "SAVED_SEARCH_TRACKS"
        const val DEFAULT_TEXT = ""
        const val TRACK = "track"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}