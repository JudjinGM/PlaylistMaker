package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.libs.MediaPlayerContract
import com.example.playlistmaker.domain.model.PlayerStatus

class MediaPlayerInitUseCaseImpl(
    private val mediaPlayer: MediaPlayerContract,
    private val callback: (PlayerStatus) -> (Unit)
) {
    fun initPlayer(urlForMusicPreview: String) {
        setListeners({ callback.invoke(PlayerStatus.STATE_PREPARED) },
            { callback.invoke(PlayerStatus.STATE_PREPARED) },
            { callback.invoke(PlayerStatus.STATE_ERROR) })
        mediaPlayer.initMediaPlayer(urlForMusicPreview)
    }

    private fun setListeners(
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
        onOnErrorListener: () -> Unit,
    ) {
        mediaPlayer.setOnPreparedListener(onPreparedListener)
        mediaPlayer.setOnCompletionListener(onCompletionListener)
        mediaPlayer.setOnErrorListener(onOnErrorListener)
    }
}