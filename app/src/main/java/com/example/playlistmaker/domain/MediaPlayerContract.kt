package com.example.playlistmaker.domain

interface MediaPlayerContract {
    fun initMediaPlayer(url:String)
    fun play()
    fun pause()
    fun stop()
    fun release()
    fun setOnPreparedListener(callback: () -> Unit)
    fun setOnCompletionListener(callback: () -> Unit)
    fun setOnErrorListener(callback: () -> Unit)
    fun getCurrentPosition():Long
}