package com.example.playlistmaker.presenter

import com.example.playlistmaker.data.MediaPlayerImplementation
import com.example.playlistmaker.domain.model.Track

class AudioPlayerPresenterCreator(private val track: Track) {
    private val mediaPlayer = MediaPlayerImplementation()
    fun createPresenter(view: AudioPlayerView): AudioPlayerPresenter {
        return AudioPlayerPresenter(view = view, mediaPlayer = mediaPlayer, track = track)
    }
}