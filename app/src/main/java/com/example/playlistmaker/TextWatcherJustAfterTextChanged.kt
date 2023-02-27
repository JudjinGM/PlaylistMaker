package com.example.playlistmaker

import android.text.TextWatcher

interface TextWatcherJustAfterTextChanged : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
}