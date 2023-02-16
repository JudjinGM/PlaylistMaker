package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
    }

    var inputSearchText: String? = null
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SAVED_TEXT, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.iconBackSearch)
        buttonBack.setOnClickListener {
            finish()
        }

        val inputSearchField = findViewById<EditText>(R.id.searchEditText)
        inputSearchField.setText(inputSearchText)

        val clearButton = findViewById<ImageView>(R.id.searchClearIcon)
        clearButton.setOnClickListener {
            inputSearchField.setText("")

            val inputMethodManagerClearButton =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManagerClearButton?.hideSoftInputFromWindow(inputSearchField.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                inputSearchText = inputSearchField.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputSearchField.addTextChangedListener(textWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE
        else View.VISIBLE
    }
}
