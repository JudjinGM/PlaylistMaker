package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.libraries.MediaPlayerContract
import com.example.playlistmaker.domain.model.PlayerStatus

class MediaPlayerControlUseCase(
    private val mediaPlayer: MediaPlayerContract, private val callback: (PlayerStatus) -> (Unit)
) {
    fun playPlayer() {
        mediaPlayer.play()
        callback.invoke(PlayerStatus.STATE_PLAYING)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        callback.invoke(PlayerStatus.STATE_PAUSED)
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }
}