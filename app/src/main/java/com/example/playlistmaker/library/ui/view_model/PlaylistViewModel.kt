package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.ui.activity.model.PlaylistsError
import com.example.playlistmaker.library.ui.activity.model.PlaylistsState

class PlaylistViewModel : ViewModel() {

    private var playlistState = MutableLiveData<PlaylistsState>()

    init {
        postState(PlaylistsState.Error(PlaylistsError.EMPTY))
    }

    fun observeState(): LiveData<PlaylistsState> = playlistState

    private fun postState(state: PlaylistsState) {
        playlistState.postValue(state)
    }
}