package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.model.PlayerStatus

class MediaPlayerPlaybackControlUseCase(
    private val mediaPlayerControlUseCase: MediaPlayerControlUseCase,
    private val callback: (PlayerStatus) -> (Unit)
) {
    fun execute(playerStatus: PlayerStatus) {
        when (playerStatus) {
            PlayerStatus.STATE_PLAYING -> {
                mediaPlayerControlUseCase.pausePlayer()
            }
            PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED -> {
                mediaPlayerControlUseCase.playPlayer()
            }
            PlayerStatus.STATE_ERROR -> {
                callback.invoke(PlayerStatus.STATE_ERROR)
            }
            PlayerStatus.STATE_DEFAULT -> {
                callback.invoke(PlayerStatus.STATE_DEFAULT)
            }
        }
    }
}