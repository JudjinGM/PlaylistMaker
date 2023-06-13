package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.domain.model.PlayerStatus
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract

interface MediaPlayerInitUseCase {
    fun initPlayer(urlForMusicPreview: String)

    class Base(
        private val mediaPlayer: MediaPlayerContract,
        private val callback: (PlayerStatus) -> (Unit)
    ) : MediaPlayerInitUseCase {
        override fun initPlayer(urlForMusicPreview: String) {
            setListeners({ callback.invoke(PlayerStatus.STATE_PREPARED) },
                { callback.invoke(PlayerStatus.STATE_ERROR) })
            mediaPlayer.initMediaPlayer(urlForMusicPreview)
        }

        private fun setListeners(
            onPreparedListener: () -> Unit,
            onOnErrorListener: () -> Unit,
        ) {
            mediaPlayer.setOnPreparedListener(onPreparedListener)
            mediaPlayer.setOnErrorListener(onOnErrorListener)
        }
    }
}