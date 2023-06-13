package com.example.playlistmaker.audio_player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract

class MediaPlayerImpl : MediaPlayerContract {

    private val mediaPlayer = MediaPlayer()
    private var onPreparedListener: (() -> Unit)? = null
    private var onCompletionListener: (() -> Unit)? = null
    private var onErrorListener: (() -> Unit)? = null

    override fun initMediaPlayer(url: String) {
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                onPreparedListener?.invoke()
            }// source is url so Async then
        } catch (e: java.lang.Exception) {
            onErrorListener?.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletionListener?.invoke()
            mediaPlayer.seekTo(0) // to reset currentPosition = 0
        }
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun setOnPreparedListener(callback: () -> Unit) {
        onPreparedListener = callback
    }

    override fun setOnCompletionListener(callback: () -> Unit) {
        onCompletionListener = callback
    }

    override fun setOnErrorListener(callback: () -> Unit) {
        onErrorListener = callback
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

}