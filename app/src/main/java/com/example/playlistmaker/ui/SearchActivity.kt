package com.example.playlistmaker.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.local.database.TracksDatabase
import com.example.playlistmaker.data.model.*
import com.example.playlistmaker.data.repositorie.SearchRepository
import com.example.playlistmaker.network.RetrofitInit
import com.example.playlistmaker.ui.adapter.TracksAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var inputSearchField: EditText
    private lateinit var placeholderImage: ImageView
    private lateinit var errorTextTextView: TextView
    private lateinit var refreshButton: Button

    private var inputSearchText: String = DEFAULT_TEXT

    private var retrofit = RetrofitInit()
    private val itunesService = retrofit.getService()

    private val tracksDatabase = TracksDatabase.getInstance()
    private val searchRepository = SearchRepository(itunesService, tracksDatabase)

    private val trackAdapter = TracksAdapter(tracksDatabase.tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val backImageView = findViewById<ImageView>(R.id.iconBackSearch)
        backImageView.setOnClickListener {
            finish()
        }

        inputSearchField = findViewById(R.id.searchEditText)

        val clearImageView = findViewById<ImageView>(R.id.searchClearIcon)
        clearImageView.setOnClickListener {
            inputSearchField.setText(DEFAULT_TEXT)
            clearTrackList()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputSearchField.windowToken, 0)
        }

        val textWatcher = object : TextWatcherJustAfterTextChanged {

            override fun afterTextChanged(s: Editable?) {
                clearImageView.isVisible = checkImageViewVisibility(s)
                inputSearchText = inputSearchField.text.toString()
            }
        }

        inputSearchField.addTextChangedListener(textWatcher)

        val tracksRecyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView)
        tracksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecyclerView.adapter = trackAdapter

        placeholderImage = findViewById(R.id.placeholderSearch)
        errorTextTextView = findViewById(R.id.errorTextTextView)
        refreshButton = findViewById(R.id.refreshButton)

        inputSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRepository.search(inputSearchText, (object : CallbackUpdate {
                    override fun update(oldList: MutableList<Track>) {
                        updateAdapterDiffUtil(
                            oldList, searchRepository.getTracks(), trackAdapter
                        )
                    }
                }), (object : CallbackShow {
                    override fun show(responseStatusCodes: ResponseStatusCodes) {
                        showMessage(responseStatusCodes)
                    }
                })
                )
                true
            }
            false
        }

        refreshButton.setOnClickListener {

            searchRepository.search(inputSearchText, (object : CallbackUpdate {

                override fun update(oldList: MutableList<Track>) {
                    updateAdapterDiffUtil(oldList, searchRepository.getTracks(), trackAdapter)
                }

            }), (object : CallbackShow {
                override fun show(responseStatusCodes: ResponseStatusCodes) {
                    showMessage(responseStatusCodes)
                }
            }))
        }
    }

    private fun showMessage(statusCode: ResponseStatusCodes) {
        when (statusCode) {
            ResponseStatusCodes.OK -> {
                placeholderImage.visibility = View.GONE
                errorTextTextView.visibility = View.GONE
                refreshButton.visibility = View.GONE
            }
            ResponseStatusCodes.NOTHING_FOUND -> {
                clearTrackList()
                refreshButton.visibility = View.GONE
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.error_search)
                errorTextTextView.visibility = View.VISIBLE
                errorTextTextView.text = getText(R.string.error_search)
            }
            ResponseStatusCodes.NO_CONNECTION -> {
                clearTrackList()
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.error_internet)
                errorTextTextView.visibility = View.VISIBLE
                errorTextTextView.text = getText(R.string.error_internet)
                refreshButton.visibility = View.VISIBLE
            }
        }
    }

    private fun updateAdapterDiffUtil(
        oldList: List<Track>, newList: List<Track>, tracksAdapter: TracksAdapter
    ) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })
        diffResult.dispatchUpdatesTo(tracksAdapter)
    }

    private fun clearTrackList() {
        val updatedTrack = emptyList<Track>()
        searchRepository.clearTracks()
        updateAdapterDiffUtil(oldList = searchRepository.getTracks(), newList = updatedTrack, trackAdapter)
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

    private companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val DEFAULT_TEXT = ""
    }
}