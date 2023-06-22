package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.ui.model.PlayerStatus

interface MediaPlayerControlInteractor {
    fun playPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun initPlayer(urlForMusicPreview: String)
    fun setOnCompletionListener(callback: () -> Unit)
    fun getMediaPlayerCurrentPosition(): Long
    fun getMediaPlayerStatus(): PlayerStatus

}