package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {

    private var inputSearchText: String? = null
    private val defaultText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backImageView = findViewById<ImageView>(R.id.iconBackSearch)
        backImageView.setOnClickListener {
            finish()
        }

        val inputSearchField = findViewById<EditText>(R.id.searchEditText)
        inputSearchField.setText(inputSearchText)

        val clearImageView = findViewById<ImageView>(R.id.searchClearIcon)
        clearImageView.setOnClickListener {
            inputSearchField.setText(defaultText)
            hideKeyboard(inputSearchField)
        }

        val textWatcher = object : TextWatcherJustAfterTextChanged {

            override fun afterTextChanged(s: Editable?) {
                clearImageView.isVisible = clearImageViewVisibility(s)
                inputSearchText = inputSearchField.text.toString()
            }
        }

        inputSearchField.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SAVED_TEXT, defaultText)
    }

    private fun clearImageViewVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
    }
}
