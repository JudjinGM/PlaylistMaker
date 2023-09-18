package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.useCase.GetPlaylistListUseCase
import com.example.playlistmaker.library.ui.model.PlaylistsError
import com.example.playlistmaker.library.ui.model.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val getPlaylistListUseCase: GetPlaylistListUseCase) : ViewModel() {

    private var playlistState = MutableLiveData<PlaylistsState>()

    init {
        updateState()
    }

    fun observeState(): LiveData<PlaylistsState> = playlistState

    private fun setState(state: PlaylistsState) {
        playlistState.value = state
    }

    private fun updateState() {
        setState(PlaylistsState.Loading)
        viewModelScope.launch {
            getPlaylistListUseCase.execute().collect { playlists ->
                if (playlists.isEmpty()) {
                    setState(PlaylistsState.Error(PlaylistsError.EMPTY))
                } else {
                    setState(PlaylistsState.Success.PlaylistContent(playlists))
                }
            }
        }
    }
}