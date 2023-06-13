package com.example.playlistmaker.search.ui.activity

import android.text.Editable
import android.text.TextWatcher

interface TextWatcherJustOnTextChanged : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun afterTextChanged(s: Editable?) = Unit
}
