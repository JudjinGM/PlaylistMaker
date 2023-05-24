package com.example.playlistmaker.presenter.search

import com.example.playlistmaker.domain.model.PlaceholderStatus
import com.example.playlistmaker.domain.model.Track

interface SearchView {
    fun updateAdapter(trackList: List<Track>)
    fun showPlaceholder(status: PlaceholderStatus)
}