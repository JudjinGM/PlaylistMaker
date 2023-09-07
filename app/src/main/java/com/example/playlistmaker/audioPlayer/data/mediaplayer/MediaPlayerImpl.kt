package com.example.playlistmaker.audioPlayer.data.mediaplayer

import android.media.MediaPlayer
import com.example.playlistmaker.audioPlayer.domain.repository.MediaPlayerContract

class MediaPlayerImpl(
    private val mediaPlayer: MediaPlayer
) : MediaPlayerContract {

    private var onPreparedListener: (() -> Unit)? = null
    private var onCompletionListener: (() -> Unit)? = null
    private var onErrorListener: (() -> Unit)? = null
    private var onPlayListener: (() -> Unit)? = null
    private var onPauseListener: (() -> Unit)? = null
    private var onStopListener: (() -> Unit)? = null

    override fun initMediaPlayer(url: String) {
        if (url.isEmpty()) {
            onErrorListener?.invoke()
        } else {
            try {
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()  // source is url so Async then
                mediaPlayer.setOnPreparedListener {
                    onPreparedListener?.invoke()
                }
            } catch (e: java.lang.Exception) {
                onErrorListener?.invoke()
            }
            mediaPlayer.setOnCompletionListener {
                onCompletionListener?.invoke()
                mediaPlayer.seekTo(0) // to reset currentPosition = 0
            }
        }
    }

    override fun play() {
        mediaPlayer.start()
        onPlayListener?.invoke()
    }

    override fun pause() {
        mediaPlayer.pause()
        onPauseListener?.invoke()
    }

    override fun stop() {
        mediaPlayer.stop()
        onStopListener?.invoke()
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

    override fun setOnPlayListener(callback: () -> Unit) {
        onPlayListener = callback
    }

    override fun setOnPauseListener(callback: () -> Unit) {
        onPauseListener = callback
    }

    override fun setOnStopListener(callback: () -> Unit) {
        onStopListener = callback
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}