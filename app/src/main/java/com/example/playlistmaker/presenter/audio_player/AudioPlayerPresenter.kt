package com.example.playlistmaker.presenter.audio_player

import com.example.playlistmaker.domain.libraries.MediaPlayerContract
import com.example.playlistmaker.domain.model.PlayerStatus
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.usecases.MediaPlayerControlUseCase
import com.example.playlistmaker.domain.usecases.MediaPlayerInitUseCase
import com.example.playlistmaker.domain.usecases.MediaPlayerPlaybackControlUseCase

class AudioPlayerPresenter(
    private val view: AudioPlayerView,
    private val mediaPlayer: MediaPlayerContract,
    track: Track,
) {
    private var playerState = PlayerStatus.STATE_DEFAULT
    private val mediaPlayerInitUseCase: MediaPlayerInitUseCase =
        MediaPlayerInitUseCase.Base(mediaPlayer) { playerStatus ->
            playerState = playerStatus
        }
    private val mediaPlayerControlUseCase: MediaPlayerControlUseCase =
        MediaPlayerControlUseCase.Base(mediaPlayer) { playerStatus ->
            playerState = playerStatus
        }
    private val mediaPlayerPlaybackControlUseCase: MediaPlayerPlaybackControlUseCase =
        MediaPlayerPlaybackControlUseCase.Base(mediaPlayerControlUseCase) { playerStatus ->
            playerState = playerStatus
        }

    init {
        mediaPlayerInitUseCase.initPlayer(track.previewUrl)
    }

    fun playClick() {
        mediaPlayerPlaybackControlUseCase.execute(playerState)
        view.uiUpdate(playerState)
    }

    fun pauseMediaPlayer() {
        mediaPlayerControlUseCase.pausePlayer()
        view.uiUpdate(playerState)
    }

    fun releaseMediaPlayer() {
        mediaPlayerControlUseCase.releasePlayer()
    }

    fun getTimeForUI(): Long {
        return mediaPlayer.getCurrentPosition()
    }
}
