package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract

interface MediaPlayerControlInteractor {
    fun playPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun initPlayer(urlForMusicPreview: String)

    fun setOnPreparedListener(callback: () -> Unit)
    fun setOnCompletionListener(callback: () -> Unit)
    fun setOnErrorListener(callback: () -> Unit)

    fun setOnPlayListener(callback: () -> Unit)

    fun setOnPauseListener(callback: () -> Unit)

    fun setOnStopListener(callback: () -> Unit)

    fun getMediaPlayerCurrentPosition(): Long

    class Base(
        private val mediaPlayer: MediaPlayerContract,
    ) : MediaPlayerControlInteractor {

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

        override fun setOnPreparedListener(callback: () -> Unit) {
            mediaPlayer.setOnPreparedListener(callback)
        }

        override fun setOnCompletionListener(callback: () -> Unit) {
            mediaPlayer.setOnCompletionListener(callback)
        }

        override fun setOnErrorListener(callback: () -> Unit) {
            mediaPlayer.setOnErrorListener(callback)
        }

        override fun setOnPlayListener(callback: () -> Unit) {
            mediaPlayer.setOnPlayListener(callback)
        }

        override fun setOnPauseListener(callback: () -> Unit) {
            mediaPlayer.setOnPauseListener(callback)
        }

        override fun setOnStopListener(callback: () -> Unit) {
            mediaPlayer.setOnStopListener(callback)
        }

        override fun getMediaPlayerCurrentPosition(): Long {
            return mediaPlayer.getCurrentPosition()
        }
    }
}