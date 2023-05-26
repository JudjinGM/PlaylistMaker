package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.libraries.MediaPlayerContract
import com.example.playlistmaker.domain.model.PlayerStatus

interface MediaPlayerControlUseCase {
    fun playPlayer()
    fun pausePlayer()
    fun releasePlayer()

    class Base(
        private val mediaPlayer: MediaPlayerContract, private val callback: (PlayerStatus) -> (Unit)
    ) : MediaPlayerControlUseCase {
        override fun playPlayer() {
            mediaPlayer.play()
            callback.invoke(PlayerStatus.STATE_PLAYING)
        }
        override fun pausePlayer() {
            mediaPlayer.pause()
            callback.invoke(PlayerStatus.STATE_PAUSED)
        }
        override fun releasePlayer() {
            mediaPlayer.release()
        }
    }
}