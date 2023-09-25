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

    private var isPrepared = false

    override fun initMediaPlayer(url: String) {
        if (url.isEmpty()) {
            onErrorListener?.invoke()
        } else {
            try {
                mediaPlayer.setOnPreparedListener {
                    onPreparedListener?.invoke()
                    isPrepared = true
                }
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()  // source is url so Async then
            } catch (e: java.lang.Exception) {
                onErrorListener?.invoke()
            }
            mediaPlayer.setOnCompletionListener {
                onCompletionListener?.invoke()
                if (isPrepared && isPlaying()) {
                    mediaPlayer.seekTo(0) // reset after complete}
                }
            }
        }
    }

    override fun play() {
        if (isPrepared) {
            mediaPlayer.start()
            onPlayListener?.invoke()
        }
    }

    override fun pause() {
        if (isPrepared && isPlaying()) {
            mediaPlayer.pause()
            onPauseListener?.invoke()
        }
    }

    override fun stop() {
        if (isPrepared) {
            mediaPlayer.stop()
            onStopListener?.invoke()
        }
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