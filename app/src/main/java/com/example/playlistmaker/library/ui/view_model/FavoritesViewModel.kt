package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.ui.activity.model.FavoritesError
import com.example.playlistmaker.library.ui.activity.model.FavoritesState

class FavoritesViewModel : ViewModel() {

    private var favoritesState = MutableLiveData<FavoritesState>()

    init {
        postState(FavoritesState.Error(FavoritesError.EMPTY))
    }

    fun observeState(): LiveData<FavoritesState> = favoritesState

    private fun postState(state: FavoritesState) {
        favoritesState.postValue(state)
    }
}