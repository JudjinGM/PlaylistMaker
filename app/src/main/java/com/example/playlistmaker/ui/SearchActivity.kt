package com.example.playlistmaker.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.data.local.database.TracksSearchStorage
import com.example.playlistmaker.data.model.CallbackShow
import com.example.playlistmaker.data.model.CallbackUpdate
import com.example.playlistmaker.data.model.PlaceholderStatus
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.repositorie.tracksRepository.SearchRepository
import com.example.playlistmaker.data.local.database.TrackListenHistoryLocalDataSource
import com.example.playlistmaker.data.local.database.TracksSearchLocalDataSource
import com.example.playlistmaker.data.local.database.TracksSearchRemoteDataSource
import com.example.playlistmaker.network.RetrofitFactory
import com.example.playlistmaker.ui.adapter.TracksAdapter


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

    private var inputSearchText: String = DEFAULT_TEXT

    private var retrofit = RetrofitFactory()
    private val itunesService = retrofit.getService()

    private val tracksSearchStorage = TracksSearchStorage

    private val tracksSearchRemote = TracksSearchRemoteDataSource(itunesService)
    private val tracksSearchLocal = TracksSearchLocalDataSource(tracksSearchStorage)
    private val trackListenHistoryLocal =
        TrackListenHistoryLocalDataSource(App.localDataSource)

    private val searchRepository =
        SearchRepository(tracksSearchRemote, tracksSearchLocal, trackListenHistoryLocal)

    private val tracksAdapter = TracksAdapter(searchRepository.getSearchTracks())

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
                if (inputSearchField.hasFocus() && s?.isEmpty() == true && !searchRepository.isListenHistoryIsEmpty()) {
                    tracksAdapter.updateAdapter(searchRepository.getListOfListenHistoryTracks())
                    showPlaceholder(PlaceholderStatus.PLACEHOLDER_HISTORY)
                } else {
                    tracksAdapter.updateAdapter(searchRepository.getSearchTracks())
                    showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
                }
            }
        }
        inputSearchField.addTextChangedListener(textWatcher)

        inputSearchField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && inputSearchField.text.isEmpty() && !searchRepository.isListenHistoryIsEmpty()) {
                tracksAdapter.updateAdapter(searchRepository.getListOfListenHistoryTracks())
                showPlaceholder(PlaceholderStatus.PLACEHOLDER_HISTORY)
            } else {
                tracksAdapter.updateAdapter(searchRepository.getSearchTracks())
                showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
            }
        }
        inputSearchField.requestFocus()
    }

    private fun viewsInit() {
        backImageView = findViewById(R.id.iconBackSearch)
        inputSearchField = findViewById(R.id.searchEditText)
        clearImageView = findViewById(R.id.searchClearIcon)
        tracksRecyclerView = findViewById(R.id.tracksRecyclerView)
        placeholderImage = findViewById(R.id.placeholderSearch)
        errorTextTextView = findViewById(R.id.errorTextTextView)
        refreshButton = findViewById(R.id.refreshButton)
        headerSearchHistoryTextView = findViewById(R.id.headerSearchHistoryTextView)
        buttonClearHistory = findViewById(R.id.buttonClearHistory)
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
            showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
            clearTrackSearchHistory()
        }

        tracksAdapter.onTrackClicked = { track ->
            searchRepository.addTrackToListenHistory(track)
            Toast.makeText(
                this,
                "${track.trackName} ${getText(R.string.added_to_recent_searches)}",
                Toast.LENGTH_SHORT
            ).show()
        }

        inputSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRepository.searchTracks(
                    inputSearchText,
                    callbackUpdate = (object : CallbackUpdate {
                        override fun update(newTracks: List<Track>) {
                            tracksAdapter.updateAdapter(newTracks)
                        }
                    }),
                    callbackShow = (object : CallbackShow {
                        override fun show(status: PlaceholderStatus) {
                            showPlaceholder(status)
                        }
                    })
                )
                true
            }
            false
        }

        refreshButton.setOnClickListener        {
            searchRepository.searchTracks(
                inputSearchText,
                callbackUpdate = (object : CallbackUpdate {
                    override fun update(newTracks: List<Track>) {
                        tracksAdapter.updateAdapter(newTracks)
                    }
                }),
                callbackShow = (object : CallbackShow {
                    override fun show(status: PlaceholderStatus) {
                        showPlaceholder(status)
                    }
                })
            )
        }
    }

    private fun clearTrackList() {
        searchRepository.clearSearchList()
        tracksAdapter.updateAdapter(searchRepository.getSearchTracks())
    }

    private fun clearTrackSearchHistory() {
        searchRepository.clearListenHistory()
        tracksAdapter.updateAdapter(searchRepository.getListOfListenHistoryTracks())
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
            PlaceholderStatus.NO_PLACEHOLDER -> {
                placeholderImage.visibility = View.GONE
                errorTextTextView.visibility = View.GONE
                refreshButton.visibility = View.GONE
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
            }
            PlaceholderStatus.PLACEHOLDER_NOTHING_FOUND -> {
                clearTrackList()
                refreshButton.visibility = View.GONE
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.error_search)
                errorTextTextView.visibility = View.VISIBLE
                errorTextTextView.text = getText(R.string.error_search)
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
            }
            PlaceholderStatus.NO_CONNECTION -> {
                clearTrackList()
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.error_internet)
                errorTextTextView.visibility = View.VISIBLE
                errorTextTextView.text = getText(R.string.error_internet)
                refreshButton.visibility = View.VISIBLE
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
            }
            PlaceholderStatus.PLACEHOLDER_HISTORY -> {
                refreshButton.visibility = View.GONE
                headerSearchHistoryTextView.visibility = View.GONE
                buttonClearHistory.visibility = View.GONE
                headerSearchHistoryTextView.visibility = View.VISIBLE
                buttonClearHistory.visibility = View.VISIBLE
            }
        }
    }

    private companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val DEFAULT_TEXT = ""
    }
}