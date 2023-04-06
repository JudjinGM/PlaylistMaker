package com.example.playlistmaker.data.model

interface CallbackUpdate {
    fun update(oldList: MutableList<Track>)
}