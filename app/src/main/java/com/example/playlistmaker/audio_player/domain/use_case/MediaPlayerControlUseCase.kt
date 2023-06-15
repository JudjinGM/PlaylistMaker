package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.domain.model.PlayerStatus
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract

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