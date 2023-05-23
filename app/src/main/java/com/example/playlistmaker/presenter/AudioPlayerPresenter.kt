package com.example.playlistmaker.presenter

import com.example.playlistmaker.data.MediaPlayerImplementation
import com.example.playlistmaker.domain.model.PlayerStatus
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.usecases.MediaPlayerControlUseCase
import com.example.playlistmaker.domain.usecases.MediaPlayerInitUseCaseImpl
import com.example.playlistmaker.domain.usecases.MediaPlayerPlaybackControlUseCase

class AudioPlayerPresenter(private val view: AudioPlayerView, track: Track) {

    private var playerState = PlayerStatus.STATE_DEFAULT
    private val mediaPlayer = MediaPlayerImplementation()
    private val mediaPlayerInitUseCase = MediaPlayerInitUseCaseImpl(mediaPlayer) { playerStatus ->
        playerState = playerStatus
    }
    private val mediaPlayerControlUseCase = MediaPlayerControlUseCase(mediaPlayer) { playerStatus ->
        playerState = playerStatus
    }
    private val mediaPlayerPlaybackControlUseCase =
        MediaPlayerPlaybackControlUseCase(mediaPlayerControlUseCase) { view.showError() }


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
        mediaPlayer.release()
    }

    fun getTimeForUI(): Long {
        return mediaPlayer.getCurrentPosition()
    }
}
