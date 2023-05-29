package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.PlaceholderStatus
import com.example.playlistmaker.domain.model.PlaceholderStatus.*
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presenter.creators.SearchPresenterCreator
import com.example.playlistmaker.presenter.search.SearchPresenter
import com.example.playlistmaker.presenter.search.SearchView
import com.example.playlistmaker.ui.audio_player.AudioPlayerActivity

class SearchActivity : AppCompatActivity(), SearchView {

    private lateinit var inputSearchField: EditText
    private lateinit var placeholderImage: ImageView
    private lateinit var errorTextTextView: TextView
    private lateinit var refreshButton: Button
    private lateinit var headerSearchHistoryTextView: TextView
    private lateinit var buttonClearHistory: Button
    private lateinit var clearImageView: ImageView
    private lateinit var tracksRecyclerView: RecyclerView
    private lateinit var backImageView: ImageView
    private lateinit var progressBar: ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var presenter: SearchPresenter

    private var inputSearchText: String = DEFAULT_TEXT
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val creator = SearchPresenterCreator()
        presenter = creator.createPresenter(this)

        viewsInit()
        recycleViewInit()
        setOnClicksAndActions()
        setOnTextWatchersTextChangeListeners()
    }

    private fun viewsInit() {
        backImageView = findViewById(R.id.backSearchImageView)
        inputSearchField = findViewById(R.id.searchEditText)
        clearImageView = findViewById(R.id.searchClearIcon)
        tracksRecyclerView = findViewById(R.id.tracksRecyclerView)
        placeholderImage = findViewById(R.id.placeholderSearch)
        errorTextTextView = findViewById(R.id.errorTextTextView)
        refreshButton = findViewById(R.id.refreshButton)
        headerSearchHistoryTextView = findViewById(R.id.headerSearchHistoryTextView)
        buttonClearHistory = findViewById(R.id.buttonClearHistory)
        progressBar = findViewById(R.id.progressBar)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SAVED_TEXT, DEFAULT_TEXT)
        inputSearchField.setText(inputSearchText)
    }

    override fun showPlaceholder(status: PlaceholderStatus) {
        when (status) {
            NO_PLACEHOLDER -> {
                placeholderImage.visibility = View.GONE
                errorTextTextView.visibility = View.GONE
                refreshButton.visibility = View.GONE
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            PLACEHOLDER_NOTHING_FOUND -> {
                refreshButton.visibility = View.GONE
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.error_search)
                errorTextTextView.visibility = View.VISIBLE
                errorTextTextView.text = getText(R.string.error_search)
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            PLACEHOLDER_NO_CONNECTION -> {
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.error_internet)
                errorTextTextView.visibility = View.VISIBLE
                errorTextTextView.text = getText(R.string.error_internet)
                refreshButton.visibility = View.VISIBLE
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            PLACEHOLDER_HISTORY -> {
                placeholderImage.visibility = View.GONE
                errorTextTextView.visibility = View.GONE
                refreshButton.visibility = View.GONE
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
                headerSearchHistoryTextView.visibility = View.VISIBLE
                buttonClearHistory.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
            PLACEHOLDER_PROGRESS_BAR -> {
                placeholderImage.visibility = View.GONE
                errorTextTextView.visibility = View.GONE
                refreshButton.visibility = View.GONE
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun updateAdapter(trackList: List<Track>) {
        tracksAdapter.updateAdapter(trackList)
    }

    private fun setOnClicksAndActions() {

        backImageView.setOnClickListener {
            finish()
        }

        clearImageView.setOnClickListener {
            presenter.showEmptySearch()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputSearchField.windowToken, 0)
            inputSearchField.setText(DEFAULT_TEXT)
        }

        buttonClearHistory.setOnClickListener {
            presenter.showEmptyListenHistory()
        }

        tracksAdapter.onTrackClicked = { track ->
            if (clickDebounce()) {
                presenter.addTrackToListenHistory(track)
                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra(TRACK, track)
                startActivity(intent)
            }
        }

        refreshButton.setOnClickListener {
            presenter.searchRequest(inputSearchText)
        }
    }

    private fun setOnTextWatchersTextChangeListeners() {
        val textWatcher = object : TextWatcherJustOnTextChanged {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearImageView.isVisible = checkImageViewVisibility(s)
                inputSearchText = inputSearchField.text.toString()

                if (inputSearchField.hasFocus() && (s?.isEmpty() == true || s?.isBlank() == true) && presenter.provideIsListenHistoryNotEmpty()) {
                    presenter.showListenHistory()
                } else if (inputSearchField.hasFocus() && (s?.isEmpty() == true || s?.isBlank() == true)) {
                    presenter.showEmptySearch()
                } else {
                    searchDebounced()
                    presenter.showSearchedTracks()
                }
            }
        }
        inputSearchField.addTextChangedListener(textWatcher)

        inputSearchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputSearchField.text.isEmpty() && presenter.provideIsListenHistoryNotEmpty()) {
                presenter.showListenHistory()
            } else {
                presenter.showSearchedTracks()
            }
        }
        inputSearchField.requestFocus()
    }
    private fun recycleViewInit() {
        tracksAdapter = TracksAdapter(presenter.provideSearchTracks())
        tracksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecyclerView.adapter = tracksAdapter
    }
    private fun checkImageViewVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    private fun searchRequest() {
        if (inputSearchText.isNotEmpty() || inputSearchText.isNotBlank()) {
            presenter.searchRequest(inputSearchText)
        }
    }

    private fun searchDebounced() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val DEFAULT_TEXT = ""
        const val TRACK = "track"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}