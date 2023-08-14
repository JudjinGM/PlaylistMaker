package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.use_case.GetFavoriteTrackUseCase
import com.example.playlistmaker.library.ui.model.FavoritesError
import com.example.playlistmaker.library.ui.model.FavoritesState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.use_case.AddTrackToListenHistoryUseCase
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteTrackUseCase: GetFavoriteTrackUseCase,
    private val addTrackToListenHistoryUseCase: AddTrackToListenHistoryUseCase
) : ViewModel() {

    private var stateLiveData = MutableLiveData<FavoritesState>()

    init {
        updateState()
    }

    fun observeState(): LiveData<FavoritesState> = stateLiveData

    private fun setState(state: FavoritesState) {
        stateLiveData.value = state
    }

    private fun updateState() {
        viewModelScope.launch {
            getFavoriteTrackUseCase.execute().collect { tracks ->
                if (tracks.isEmpty()) {
                    setState(FavoritesState.Error(FavoritesError.EMPTY))
                } else {
                    setState(FavoritesState.Success.FavoriteContent(tracks))
                }
            }
        }
    }

    fun addToListenHistory(track: Track) {
        addTrackToListenHistoryUseCase.execute(track)
    }
}