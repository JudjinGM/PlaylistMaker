package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract

interface GetMediaPlayerCurrentPositionUseCase {
    fun execute(): Long

    class Base(private val mediaPlayer: MediaPlayerContract) :
        GetMediaPlayerCurrentPositionUseCase {
        override fun execute(): Long {
            return mediaPlayer.getCurrentPosition()
        }
    }
}