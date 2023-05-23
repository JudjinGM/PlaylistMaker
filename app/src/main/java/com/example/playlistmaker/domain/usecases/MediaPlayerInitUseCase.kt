package com.example.playlistmaker.domain.usecases

interface MediaPlayerInitUseCase {
    fun initPlayer(urlForMusicPreview: String)
    fun setListeners(
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
        onOnErrorListener: () -> Unit,
    )
}