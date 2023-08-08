package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.ui.model.PlaylistsError
import com.example.playlistmaker.library.ui.model.PlaylistsState

class PlaylistViewModel : ViewModel() {

    private var playlistState = MutableLiveData<PlaylistsState>()

    init {
        setState(PlaylistsState.Error(PlaylistsError.EMPTY))
    }

    fun observeState(): LiveData<PlaylistsState> = playlistState

    private fun setState(state: PlaylistsState) {
        playlistState.value = state
    }
}