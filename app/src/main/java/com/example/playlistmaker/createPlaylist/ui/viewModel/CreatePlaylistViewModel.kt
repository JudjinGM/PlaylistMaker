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
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistErrorState
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase
) : ViewModel() {

    protected val stateLiveData = MutableLiveData<CreatePlaylistState>()

    protected var playlistName: String = DEFAULT_TEXT
    protected var playlistDescription: String = DEFAULT_TEXT
    protected var playlistCoverLoaded: Uri? = null
    protected var playlistCoverSavedToStorage: Uri? = null

    fun observeState(): LiveData<CreatePlaylistState> = stateLiveData

    fun getNameInput(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            stateLiveData.value = CreatePlaylistState.ButtonState(isEnable = false)
            playlistName = DEFAULT_TEXT
        } else {
            stateLiveData.value = CreatePlaylistState.ButtonState(isEnable = true)
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

    protected suspend fun saveCoverImageToStorage(uri: Uri) {
        when (val result = saveImageToPrivateStorageUseCase.execute(uri)) {
            ResultForFile.Error -> {
                stateLiveData.value =
                    CreatePlaylistState.SaveNotSuccess(CreatePlaylistErrorState.CANNOT_SAVE_IMAGE)
            }

            is ResultForFile.Success -> {
                playlistCoverSavedToStorage = result.file.toUri()
            }
        }
    }


    open fun createButtonClicked() {
        viewModelScope.launch {
            val resultDeferred = async {
                if (playlistCoverLoaded != null) {
                    playlistCoverLoaded?.let { saveCoverImageToStorage(it) }
                }
                createPlaylistUseCase.execute(
                    playlistName, playlistDescription, playlistCoverSavedToStorage
                )
            }
            resultDeferred.await()
            showAfterCreate()
            closeScreen()
        }
    }

    open fun showAfterCreate() {
        stateLiveData.value = CreatePlaylistState.SaveSuccess(playlistName)
    }

    open fun closeScreen() {
        if (playlistName.isBlank() && playlistDescription.isBlank() && playlistCoverLoaded == null) {
            stateLiveData.value = CreatePlaylistState.Navigate.Back(isNeedToConfirm = true)
        } else {
            stateLiveData.value = CreatePlaylistState.Navigate.Back(isNeedToConfirm = false)
        }
    }

    companion object {
        const val DEFAULT_TEXT = ""
    }
}