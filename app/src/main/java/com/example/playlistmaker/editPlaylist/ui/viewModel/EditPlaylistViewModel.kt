package com.example.playlistmaker.editPlaylist.ui.viewModel

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.createPlaylist.domain.useCase.CreatePlaylistUseCase
import com.example.playlistmaker.createPlaylist.domain.useCase.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistState
import com.example.playlistmaker.createPlaylist.ui.viewModel.CreatePlaylistViewModel
import com.example.playlistmaker.editPlaylist.domain.useCase.DeleteImageFromPrivateStorageUseCase
import com.example.playlistmaker.editPlaylist.domain.useCase.GetPlaylistByIdUseCase
import com.example.playlistmaker.editPlaylist.domain.useCase.UpdatePlaylistUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Long,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
    createPlaylistUseCase: CreatePlaylistUseCase,
    saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val deleteImageFromPrivateStorageUseCase: DeleteImageFromPrivateStorageUseCase
) : CreatePlaylistViewModel(createPlaylistUseCase, saveImageToPrivateStorageUseCase) {

    lateinit var playlistModel: PlaylistModel

    init {
        stateLiveData.value = CreatePlaylistState.ButtonState(isEnable = true)
        viewModelScope.launch {
            val resultDeferred = async {
                playlistModel = getPlaylistByIdUseCase.execute(playlistId)

            }
            resultDeferred.await()
            stateLiveData.value = CreatePlaylistState.InitState(playlistModel)
        }
    }

    override fun createButtonClicked() {
        viewModelScope.launch {
            val resultDeferred = async {
                if (playlistCoverLoaded != null) {
                    playlistCoverLoaded?.let {
                        saveCoverImageToStorage(it)
                        val uriForFileDelete = playlistModel.playlistCoverImage
                        if (uriForFileDelete != null) {
                            deleteImageFromPrivateStorageUseCase.execute(
                                uriForFileDelete
                            )
                        }
                    }
                } else playlistCoverSavedToStorage = playlistModel.playlistCoverImage

                updatePlaylistUseCase.execute(
                    playlistId,
                    playlistName,
                    playlistDescription,
                    playlistCoverSavedToStorage,
                    playlistModel.tracks
                )
            }
            resultDeferred.await()
            closeScreen()
        }
    }

    override fun closeScreen() {
        stateLiveData.value = CreatePlaylistState.Navigate.Back(isNeedToConfirm = false)
    }

    override fun showAfterCreate() {}
}