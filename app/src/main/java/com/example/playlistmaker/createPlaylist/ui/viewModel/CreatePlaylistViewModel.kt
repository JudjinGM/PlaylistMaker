package com.example.playlistmaker.createPlaylist.ui.viewModel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.data.model.ResultForFile
import com.example.playlistmaker.createPlaylist.domain.useCase.CreatePlaylistUseCase
import com.example.playlistmaker.createPlaylist.domain.useCase.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.createPlaylist.ui.model.BackState
import com.example.playlistmaker.createPlaylist.ui.model.CreateButtonState
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistErrorState
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistScreenState
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase
) : ViewModel() {

    protected val toastStateLiveData = MutableLiveData<CreatePlaylistState>()
    protected val createButtonState = MutableLiveData<CreateButtonState>(CreateButtonState.Disabled)
    protected val backBehaviourState = MutableLiveData<BackState>(BackState.Success)
    protected val createPlaylistScreenState = MutableLiveData<CreatePlaylistScreenState>()

    protected var playlistName: String = DEFAULT_TEXT
    protected var playlistDescription: String = DEFAULT_TEXT
    protected var playlistCoverLoaded: Uri? = null
    protected var playlistCoverSavedToStorage: Uri? = null

    fun observeToastState(): LiveData<CreatePlaylistState> = toastStateLiveData
    fun observeCreateButtonState(): LiveData<CreateButtonState> = createButtonState

    fun observeBackBehaviourState(): LiveData<BackState> = backBehaviourState

    fun observeCreatePlaylistScreenState(): LiveData<CreatePlaylistScreenState> =
        createPlaylistScreenState

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
        playlistDescription = if (s.isNullOrEmpty()) {
            DEFAULT_TEXT
        } else {
            s.toString()
        }
    }

    fun getCoverImageLoadedUri(uri: Uri?) {
        playlistCoverLoaded = uri
    }

    protected fun saveCoverImageToStorage(uri: Uri) {
        viewModelScope.launch {
            val result = saveImageToPrivateStorageUseCase.execute(uri)

            when (result) {
                ResultForFile.Error -> {
                    toastStateLiveData.value =
                        CreatePlaylistState.Error(CreatePlaylistErrorState.CANNOT_SAVE_IMAGE)
                }

                is ResultForFile.Success -> {
                    playlistCoverSavedToStorage = result.file.toUri()
                }
            }
        }
    }

    fun createButtonClicked() {
        if (playlistCoverLoaded != null) {
            playlistCoverLoaded?.let { saveCoverImageToStorage(it) }
        }

        if (createButtonState.value == CreateButtonState.Enabled) {
            viewModelScope.launch {
                val resultDeferred = async {
                    createPlaylistUseCase.execute(
                        playlistName, playlistDescription, playlistCoverSavedToStorage
                    )
                }
                resultDeferred.await()
                closeScreen()
                toastStateLiveData.value = CreatePlaylistState.Success(playlistName)
            }
        }
    }

    fun closeScreen() {
        createPlaylistScreenState.value = CreatePlaylistScreenState.CloseScreen
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