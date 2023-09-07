package com.example.playlistmaker.createPlaylist.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.useCase.CreatePlaylistUseCase
import com.example.playlistmaker.createPlaylist.ui.model.BackState
import com.example.playlistmaker.createPlaylist.ui.model.CreateButtonState
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistState
import com.example.playlistmaker.createPlaylist.ui.model.SaveImageState
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistUseCase: CreatePlaylistUseCase
) : ViewModel() {

    private val toastStateLiveData = MutableLiveData<CreatePlaylistState>()
    private val createButtonState = MutableLiveData<CreateButtonState>(CreateButtonState.Disabled)
    private val backBehaviourState = MutableLiveData<BackState>(BackState.Success)
    private val saveImageToStorageState = MutableLiveData<SaveImageState>()

    private var playlistName: String = DEFAULT_TEXT
    private var playlistDescription: String = DEFAULT_TEXT
    private var playlistCoverLoaded: Uri? = null
    private var playlistCoverSavedToStorage: Uri? = null

    fun observeToastState(): LiveData<CreatePlaylistState> = toastStateLiveData
    fun observeCreateButtonState(): LiveData<CreateButtonState> = createButtonState

    fun observeBackBehaviourState(): LiveData<BackState> = backBehaviourState

    fun saveImageToStorageState(): LiveData<SaveImageState> = saveImageToStorageState

    fun getNameInput(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            createButtonState.value = CreateButtonState.Disabled
            playlistName = DEFAULT_TEXT
        } else {
            createButtonState.value = CreateButtonState.Enabled
            playlistName = s.toString()
        }
    }

    fun getDescriptionInput(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            playlistDescription = DEFAULT_TEXT
        } else {
            playlistDescription = s.toString()
        }
    }

    fun getCoverImageLoadedUri(uri: Uri?) {
        playlistCoverLoaded = uri
    }

    fun getCoverImageSavedToStorageUri(uri: Uri) {
        playlistCoverSavedToStorage = uri
    }

    fun createButtonClicked() {
        if (playlistCoverLoaded != null) {
            saveImageToStorageState.value =
                playlistCoverLoaded?.let { SaveImageState.Allow(uri = it) }
        }

        if (createButtonState.value == CreateButtonState.Enabled) viewModelScope.launch {
            createPlaylistUseCase.execute(
                playlistName, playlistDescription, playlistCoverSavedToStorage
            )
        }

        toastStateLiveData.value = CreatePlaylistState.Success(playlistName)
        Log.d("judjin", playlistCoverSavedToStorage.toString())
    }

    fun backStateBehaviour() {
        if (playlistName.isBlank() && playlistDescription.isBlank() && playlistCoverLoaded == null) {
            backBehaviourState.value = BackState.Success
        } else backBehaviourState.value = BackState.Error
    }

    companion object {
        const val DEFAULT_TEXT = ""
    }

}