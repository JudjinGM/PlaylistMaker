package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.domain.model.PlayerStatus

interface MediaPlayerPlaybackControlUseCase {
    fun execute(playerStatus: PlayerStatus)
    class Base(
        private val mediaPlayerControlUseCase: MediaPlayerControlUseCase,
        private val callback: (PlayerStatus) -> (Unit)
    ) : MediaPlayerPlaybackControlUseCase {
        override fun execute(playerStatus: PlayerStatus) {
            when (playerStatus) {
                PlayerStatus.STATE_PLAYING -> {
                    mediaPlayerControlUseCase.pausePlayer()
                    callback.invoke(PlayerStatus.STATE_PAUSED)
                }
                PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED -> {
                    mediaPlayerControlUseCase.playPlayer()
                    callback.invoke(PlayerStatus.STATE_PLAYING)
                }
                PlayerStatus.STATE_ERROR -> {
                    callback.invoke(PlayerStatus.STATE_ERROR)
                }
                PlayerStatus.STATE_NETWORK_ERROR -> {
                    callback.invoke(PlayerStatus.STATE_NETWORK_ERROR)
                }
                PlayerStatus.STATE_DEFAULT -> {
                    callback.invoke(PlayerStatus.STATE_DEFAULT)
                }
            }
        }
    }
}