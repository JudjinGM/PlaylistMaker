package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private var inputSearchField: EditText? = null
    private var inputSearchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backImageView = findViewById<ImageView>(R.id.iconBackSearch)
        backImageView.setOnClickListener {
            finish()
        }

        inputSearchField = findViewById<EditText>(R.id.searchEditText)

        val clearImageView = findViewById<ImageView>(R.id.searchClearIcon)
        clearImageView.setOnClickListener {
            inputSearchField?.setText(DEFAULT_TEXT)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputSearchField?.windowToken, 0)
        }

        val textWatcher = object : TextWatcherJustAfterTextChanged {

            override fun afterTextChanged(s: Editable?) {
                clearImageView.isVisible = checkImageViewVisibility(s)
                inputSearchText = inputSearchField?.text.toString()
            }
        }

        inputSearchField?.addTextChangedListener(textWatcher)

        val listOfTrackMock = ListOfTrackMock()

        val tracksRecyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView)
        tracksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val trackAdapter = TracksAdapter(listOfTrackMock.getList())
        tracksRecyclerView.adapter = trackAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SAVED_TEXT, DEFAULT_TEXT)
        inputSearchField?.setText(inputSearchText)
    }

    private fun checkImageViewVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val DEFAULT_TEXT = ""
    }
}