package com.example.playlistmaker.presenter.creators

import com.example.playlistmaker.data.libraries.MediaPlayerImplementation
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presenter.audio_player.AudioPlayerPresenter
import com.example.playlistmaker.presenter.audio_player.AudioPlayerView

class AudioPlayerPresenterCreator(private val track: Track) {
    private val mediaPlayer = MediaPlayerImplementation()
    fun createPresenter(view: AudioPlayerView): AudioPlayerPresenter {
        return AudioPlayerPresenter(view = view, mediaPlayer = mediaPlayer, track = track)
    }
}