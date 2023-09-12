package com.example.playlistmaker.audioPlayer.domain.repository

interface MediaPlayerContract {
    fun initMediaPlayer(url: String)
    fun play()
    fun pause()
    fun stop()
    fun release()
    fun setOnPreparedListener(callback: () -> Unit)
    fun setOnCompletionListener(callback: () -> Unit)
    fun setOnErrorListener(callback: () -> Unit)
    fun setOnPlayListener(callback: () -> Unit)
    fun setOnPauseListener(callback: () -> Unit)
    fun setOnStopListener(callback: () -> Unit)
    fun getCurrentPosition(): Long

    fun isPlaying(): Boolean
}