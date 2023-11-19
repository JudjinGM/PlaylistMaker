package com.example.playlistmaker.playlist.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.editPlaylist.domain.useCase.DeleteImageFromPrivateStorageUseCase
import com.example.playlistmaker.playlist.domain.useCase.DeletePlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.GetPlaylistFlowUseCase
import com.example.playlistmaker.playlist.domain.useCase.SharePlaylistUseCase
import com.example.playlistmaker.playlist.ui.model.PlaylistState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Long,
    private val getPlaylistFlowUseCase: GetPlaylistFlowUseCase,
    private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val sharePlaylistUseCase: SharePlaylistUseCase,
    private val deleteImageFromPrivateStorageUseCase: DeleteImageFromPrivateStorageUseCase
) : ViewModel() {

    private var playlistStateLiveData = MutableLiveData<PlaylistState>()

    private var isTracksNotEmpty: Boolean = false

    private var playlistModel: PlaylistModel? = null

    init {
        viewModelScope.launch {
            getPlaylistFlowUseCase.execute(playlistId).collect {
                playlistStateLiveData.value = PlaylistState.Success(it)
                isTracksNotEmpty = it.tracks.isNotEmpty()
                playlistModel = it
            }
        }
    }

    fun observePlaylistState(): LiveData<PlaylistState> = playlistStateLiveData

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            deleteTrackFromPlaylistUseCase.execute(playlistId, track)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            val resultDeferred = async {
                playlistModel?.let { deletePlaylistUseCase.execute(playlistId, it) }
                playlistModel?.playlistCoverImage?.let {
                    deleteImageFromPrivateStorageUseCase.execute(it)
                }
            }
            resultDeferred.await()
            playlistStateLiveData.value = PlaylistState.Navigate.Back
        }
    }

    fun sharePlaylistClicked() {
        val currentState = getCurrentState()
        if (isTracksNotEmpty) {
            playlistStateLiveData.value = PlaylistState.SharePlaylistSuccess
            currentState?.let { setState(it) }
        } else {
            playlistStateLiveData.value = PlaylistState.SharePlaylistError
            currentState?.let { setState(it) }
        }
    }

    fun sharePlaylist() {
        playlistModel?.let { sharePlaylistUseCase.execute(it) }
    }

    fun getPlaylistName(): String {
        return playlistModel?.playlistName ?: DEFAULT_VALUE
    }

    fun editPlaylistClicked() {
        val currentState = getCurrentState()
        playlistStateLiveData.value = PlaylistState.Navigate.EditPlaylist(playlistId)
        currentState?.let { setState(it) }
    }

    private fun setState(state: PlaylistState) {
        playlistStateLiveData.value = state
    }

    private fun getCurrentState(): PlaylistState? {
        return when (playlistStateLiveData.value) {
            is PlaylistState.Success -> (playlistStateLiveData.value as PlaylistState.Success).copy()
            PlaylistState.Navigate.Back -> playlistStateLiveData.value as PlaylistState.Navigate.Back
            is PlaylistState.Navigate.EditPlaylist -> (playlistStateLiveData.value as PlaylistState.Navigate.EditPlaylist).copy()
            PlaylistState.SharePlaylistError -> playlistStateLiveData.value as PlaylistState.SharePlaylistError
            PlaylistState.SharePlaylistSuccess -> playlistStateLiveData.value as PlaylistState.SharePlaylistSuccess
            null -> null
        }
    }

    companion object {
        private const val DEFAULT_VALUE = ""
    }
}