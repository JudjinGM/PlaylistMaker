package com.example.playlistmaker.playlist.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.playlist.domain.useCase.DeletePlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.GetPlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.SharePlaylistUseCase
import com.example.playlistmaker.playlist.ui.model.PlaylistState
import com.example.playlistmaker.playlist.ui.model.SharePlaylistState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Long,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val sharePlaylistUseCase: SharePlaylistUseCase,
) : ViewModel() {

    private var playlistStateLiveData = MutableLiveData<PlaylistState>()

    private var sharePlaylistStateLiveData = MutableLiveData<SharePlaylistState>()

    private var isTracksNotEmpty: Boolean = false

    private var playlistModel: PlaylistModel? = null

    init {
        viewModelScope.launch {
            getPlaylistUseCase.execute(playlistId).collect {
                playlistStateLiveData.value = PlaylistState.Success(it)
                isTracksNotEmpty = it.tracks.isNotEmpty()
                playlistModel = it
            }
        }
    }

    fun observePlaylistState(): LiveData<PlaylistState> = playlistStateLiveData
    fun observeSharePlaylistState(): LiveData<SharePlaylistState> = sharePlaylistStateLiveData

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            deleteTrackFromPlaylistUseCase.execute(playlistId, track)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            val resultDeferred = async {
                playlistModel?.let { deletePlaylistUseCase.execute(playlistId, it) }
            }
            resultDeferred.await()
            playlistStateLiveData.value = PlaylistState.CloseScreen

        }
    }

    fun sharePlaylistClicked() {
        if (isTracksNotEmpty) {
            sharePlaylistStateLiveData.value = SharePlaylistState.Success
        } else {
            sharePlaylistStateLiveData.value = SharePlaylistState.Error
        }
    }

    fun sharePlaylist() {
        playlistModel?.let { sharePlaylistUseCase.execute(it) }
    }
}