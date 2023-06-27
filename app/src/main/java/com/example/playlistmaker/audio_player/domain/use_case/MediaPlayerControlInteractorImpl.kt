package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audio_player.ui.model.PlayerStatus

class MediaPlayerControlInteractorImpl(
    private val mediaPlayer: MediaPlayerContract,
    isConnectedToNetworkUseCase: IsConnectedToNetworkUseCase,
) : MediaPlayerControlInteractor {

    private var playerStatus = PlayerStatus.STATE_DEFAULT

    init {
        mediaPlayer.setOnPreparedListener {
            playerStatus = PlayerStatus.STATE_PREPARED
        }

        mediaPlayer.setOnPlayListener {
            playerStatus = PlayerStatus.STATE_PLAYING
        }

        mediaPlayer.setOnStopListener {
            playerStatus = PlayerStatus.STATE_PREPARED
        }

        mediaPlayer.setOnPauseListener {
            playerStatus = PlayerStatus.STATE_PAUSED
        }

        mediaPlayer.setOnErrorListener {
            playerStatus = PlayerStatus.STATE_ERROR
        }

        if (!isConnectedToNetworkUseCase.execute()) {
            playerStatus = PlayerStatus.STATE_NETWORK_ERROR
        }
    }

    override fun playPlayer() {
        mediaPlayer.play()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun initPlayer(urlForMusicPreview: String) {
        mediaPlayer.initMediaPlayer(urlForMusicPreview)
    }

    override fun setOnCompletionListener(callback: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            playerStatus = PlayerStatus.STATE_PREPARED
            callback.invoke()
        }
    }

    override fun getMediaPlayerCurrentPosition(): Long {
        return mediaPlayer.getCurrentPosition()
    }

    override fun getMediaPlayerStatus(): PlayerStatus {
        return playerStatus
    }


}
