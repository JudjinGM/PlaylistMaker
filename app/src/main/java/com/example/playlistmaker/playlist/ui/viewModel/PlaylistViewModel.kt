package com.example.playlistmaker.playlist.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.useCase.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.GetPlaylistUseCase
import com.example.playlistmaker.playlist.ui.model.PlaylistState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Long,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase
) : ViewModel() {

    private var playlistStateLiveData = MutableLiveData<PlaylistState>()

    init {
        viewModelScope.launch {
            getPlaylistUseCase.execute(playlistId).collect {
                playlistStateLiveData.value = PlaylistState.Success(it)
            }
        }
    }

    fun observePlaylistState(): LiveData<PlaylistState> = playlistStateLiveData

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            deleteTrackFromPlaylistUseCase.execute(playlistId, track)
        }
    }
}