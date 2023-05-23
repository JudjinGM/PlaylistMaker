package com.example.playlistmaker.ui

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
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dataSourceImpl.ListenHistoryTracksLocalDataSourceImpl
import com.example.playlistmaker.data.dataSourceImpl.SearchTracksLocalDataSourceImpl
import com.example.playlistmaker.data.dataSourceImpl.SearchTracksRemoteDataSourceImpl
import com.example.playlistmaker.data.models.RemoteDatasourceErrorStatus.NOTHING_FOUND
import com.example.playlistmaker.data.models.RemoteDatasourceErrorStatus.NO_CONNECTION
import com.example.playlistmaker.data.network.RetrofitFactory
import com.example.playlistmaker.data.repositoryImpl.ListenHistoryRepositoryImpl
import com.example.playlistmaker.data.repositoryImpl.SearchRepositoryImpl
import com.example.playlistmaker.data.storage.TracksSearchCache
import com.example.playlistmaker.domain.model.PlaceholderStatus
import com.example.playlistmaker.domain.model.PlaceholderStatus.*
import com.example.playlistmaker.presenter.adapter.TracksAdapter

class SearchActivity : AppCompatActivity() {

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

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }

    private var inputSearchText: String = DEFAULT_TEXT

    private var retrofit = RetrofitFactory()
    private val itunesService = retrofit.getService()

    private val tracksSearchCache = TracksSearchCache

    private val searchTracksRemoteDataSource = SearchTracksRemoteDataSourceImpl(itunesService)
    private val tracksSearchLocalDataSource = SearchTracksLocalDataSourceImpl(tracksSearchCache)
    private val trackListenHistoryLocalDataSource =
        ListenHistoryTracksLocalDataSourceImpl(App.tracksListenHistoryLocalDatabase)

    private val searchRepositoryImpl = SearchRepositoryImpl(
        searchTracksRemoteDataSource,
        tracksSearchLocalDataSource,
    )

    private val listenHistoryRepository = ListenHistoryRepositoryImpl(
        trackListenHistoryLocalDataSource
    )

    private val tracksAdapter = TracksAdapter(searchRepositoryImpl.getSearchTracks())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewsInit()
        setOnClicksAndAction()

        tracksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecyclerView.adapter = tracksAdapter

        val textWatcher = object : TextWatcherJustOnTextChanged {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearImageView.isVisible = checkImageViewVisibility(s)
                inputSearchText = inputSearchField.text.toString()

                if (inputSearchField.hasFocus() && (s?.isEmpty() == true || s?.isBlank() == true) && listenHistoryRepository.isListenHistoryIsNotEmpty()) {
                    tracksAdapter.updateAdapter(listenHistoryRepository.getListOfListenHistoryTracks())
                    showPlaceholder(PLACEHOLDER_HISTORY)
                } else if (inputSearchField.hasFocus() && (s?.isEmpty() == true || s?.isBlank() == true)) {
                    clearTrackList()
                    showPlaceholder(NO_PLACEHOLDER)
                } else {
                    searchDebounced()
                    tracksAdapter.updateAdapter(searchRepositoryImpl.getSearchTracks())
                    showPlaceholder(NO_PLACEHOLDER)
                }
            }
        }
        inputSearchField.addTextChangedListener(textWatcher)

        inputSearchField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && inputSearchField.text.isEmpty() && listenHistoryRepository.isListenHistoryIsNotEmpty()) {
                tracksAdapter.updateAdapter(listenHistoryRepository.getListOfListenHistoryTracks())
                showPlaceholder(PLACEHOLDER_HISTORY)
            } else {
                tracksAdapter.updateAdapter(searchRepositoryImpl.getSearchTracks())
                showPlaceholder(NO_PLACEHOLDER)
            }
        }
        inputSearchField.requestFocus()
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

    private fun setOnClicksAndAction() {

        backImageView.setOnClickListener {
            finish()
        }

        clearImageView.setOnClickListener {
            clearTrackList()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputSearchField.windowToken, 0)
            inputSearchField.setText(DEFAULT_TEXT)
        }

        buttonClearHistory.setOnClickListener {
            showPlaceholder(NO_PLACEHOLDER)
            clearTrackSearchHistory()
        }

        tracksAdapter.onTrackClicked = { track ->
            if (clickDebounce()) {
                listenHistoryRepository.addTrackToListenHistory(track)
                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra(TRACK, track)
                startActivity(intent)
            }
        }


        refreshButton.setOnClickListener {
            searchRepositoryImpl.searchTracks(inputSearchText, { newTracks ->
                tracksAdapter.updateAdapter(newTracks)
                showPlaceholder(NO_PLACEHOLDER)
            }, { errorStatus ->
                when (errorStatus) {
                    NOTHING_FOUND -> showPlaceholder(PLACEHOLDER_NOTHING_FOUND)
                    NO_CONNECTION -> showPlaceholder(PLACEHOLDER_NO_CONNECTION)
                }
            })
            showPlaceholder(PLACEHOLDER_PROGRESS_BAR)
        }
    }

    private fun clearTrackList() {
        searchRepositoryImpl.clearSearchTracks()
        tracksAdapter.updateAdapter(searchRepositoryImpl.getSearchTracks())
    }

    private fun clearTrackSearchHistory() {
        listenHistoryRepository.clearListenHistory()
        tracksAdapter.updateAdapter(listenHistoryRepository.getListOfListenHistoryTracks())
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

    private fun checkImageViewVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun showPlaceholder(status: PlaceholderStatus) {
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
                clearTrackList()
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
                clearTrackList()
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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchRequest() {
        if (inputSearchText.isNotEmpty() || inputSearchText.isNotBlank()) {
            searchRepositoryImpl.searchTracks(inputSearchText, { newTracks ->
                tracksAdapter.updateAdapter(newTracks)
                showPlaceholder(NO_PLACEHOLDER)
            }, { errorStatus ->
                when (errorStatus) {
                    NOTHING_FOUND -> showPlaceholder(PLACEHOLDER_NOTHING_FOUND)
                    NO_CONNECTION -> showPlaceholder(PLACEHOLDER_NO_CONNECTION)
                }
            })
            showPlaceholder(PLACEHOLDER_PROGRESS_BAR)
        }
    }

    private fun searchDebounced() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }


    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val DEFAULT_TEXT = ""
        const val TRACK = "track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}