package com.example.playlistmaker.presenter

import com.example.playlistmaker.domain.model.Track

class AudioPlayerPresenterCreator(private val track: Track) {
    fun createPresenter(view: AudioPlayerView): AudioPlayerPresenter {
        return AudioPlayerPresenter(view, track)
    }
}