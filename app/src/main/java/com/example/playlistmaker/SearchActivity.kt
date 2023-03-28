package com.example.playlistmaker

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var inputSearchField: EditText
    private lateinit var placeholderImage: ImageView
    private lateinit var errorTextTextView: TextView
    private lateinit var refreshButton: Button

    private var inputSearchText: String = DEFAULT_TEXT

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TracksAdapter(tracks)


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
                search()
                true
            }
            false
        }

        refreshButton.setOnClickListener {
            search() }
    }

    private fun search() {
        itunesService.search(inputSearchText).enqueue(object : Callback<TrackItunesResponse> {
            override fun onResponse(
                call: Call<TrackItunesResponse>,
                response: Response<TrackItunesResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            val diffResult = getDiffResult(
                                oldList = tracks,
                                newList = response.body()!!.results
                            )
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)
                            diffResult.dispatchUpdatesTo(trackAdapter)
                            showMessage(ResponseStatusCodes.OK)
                        } else {
                            showMessage(ResponseStatusCodes.NOTHING_FOUND)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TrackItunesResponse>, t: Throwable) {
                showMessage(ResponseStatusCodes.NO_CONNECTION)
            }
        })
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

    private fun getDiffResult(oldList: List<Track>, newList: List<Track>): DiffUtil.DiffResult {
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
        return diffResult
    }

    private fun clearTrackList(){
        val updatedTrack = emptyList<Track>()
        val diffUtil = getDiffResult(oldList = tracks, newList = updatedTrack)
        tracks.clear()
        diffUtil.dispatchUpdatesTo(trackAdapter)
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

    enum class ResponseStatusCodes {
        OK,
        NOTHING_FOUND,
        NO_CONNECTION,
    }
}