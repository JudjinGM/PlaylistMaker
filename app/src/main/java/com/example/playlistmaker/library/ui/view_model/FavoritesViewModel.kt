package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.useCase.GetFavoriteTrackUseCase
import com.example.playlistmaker.library.ui.model.FavoritesError
import com.example.playlistmaker.library.ui.model.FavoritesState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.useCase.AddTrackToListenHistoryUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteTrackUseCase: GetFavoriteTrackUseCase,
    private val addTrackToListenHistoryUseCase: AddTrackToListenHistoryUseCase
) : ViewModel() {

    private var isClickAllowed = true
    private var stateLiveData = MutableLiveData<FavoritesState>()

    init {
        updateState()
    }

    fun observeState(): LiveData<FavoritesState> = stateLiveData

    fun onTrackClicked(track: Track) {
        if (isClickDebounce()) {
            val currentState = getCurrentState()
            addTrackToListenHistoryUseCase.execute(track)
            setState(FavoritesState.NavigateToPlayer(track))
            currentState?.let { setState(it) }
        }
    }

    private fun getCurrentState(): FavoritesState? {
        return when (stateLiveData.value) {
            is FavoritesState.Error -> (stateLiveData.value as FavoritesState.Error).copy()
            FavoritesState.Loading -> (stateLiveData.value as FavoritesState.Loading)
            is FavoritesState.NavigateToPlayer -> (stateLiveData.value as FavoritesState.NavigateToPlayer).copy()
            is FavoritesState.Success.FavoriteContent -> (stateLiveData.value as FavoritesState.Success.FavoriteContent).copy()
            null -> null
        }
    }

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

    private fun isClickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}