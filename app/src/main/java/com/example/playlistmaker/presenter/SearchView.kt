package com.example.playlistmaker.presenter

import com.example.playlistmaker.domain.model.PlaceholderStatus
import com.example.playlistmaker.domain.model.Track

interface SearchView {
    fun updateAdapter(trackList: List<Track>)
    fun showPlaceholder(status: PlaceholderStatus)
}