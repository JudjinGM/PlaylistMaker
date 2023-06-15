package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.model.ErrorStatus
import com.example.playlistmaker.search.domain.model.SearchState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.use_case.*
import com.example.playlistmaker.search.ui.model.SavedTracks

class SearchViewModel(
    private val addTrackToListenHistoryUseCase: AddTrackToListenHistoryUseCase,
    private val clearListenHistoryTracksUseCase: ClearListenHistoryTracksUseCase,
    private val clearSearchResultTracksUseCase: ClearSearchResultTracksUseCase,
    private val getIsListenHistoryTracksNotEmptyUseCase: GetIsListenHistoryTracksNotEmptyUseCase,
    private val getListenHistoryTracksUseCase: GetListenHistoryTracksUseCase,
    private val getSearchResultTracksUseCase: GetSearchResultTracksUseCase,
    private val getIsSearchResultIsEmptyUseCase: GetIsSearchResultIsEmptyUseCase,
    private val searchSongsUseCase: SearchSongsUseCase,
    private val addTracksToSearchResultUseCase: AddTracksToSearchResultUseCase
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val savedTracksLiveData = MutableLiveData<List<Track>>()
    fun observeSavedTracks(): LiveData<List<Track>> = savedTracksLiveData

    fun init(savedTracks: SavedTracks?) {
        val tracks = savedTracks?.tracks?.toList() ?: listOf()
        if (tracks.isNotEmpty()) {
            renderSavedTracks(tracks)
            addTracksToSearchResultUseCase.execute(tracks)
            renderState(SearchState.SearchContent(getSearchResultTracksUseCase.execute()))
        } else {
            if (getIsListenHistoryTracksNotEmptyUseCase.execute()) {
                renderState(SearchState.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
            } else renderState(SearchState.Empty)
        }
    }

    private fun renderSavedTracks(tracks: List<Track>) {
        savedTracksLiveData.postValue(tracks)
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun searchDebounced(changedText: String) {
        if (changedText == latestSearchText) {
            return
        }
        latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY_MILLIS
        handler.postAtTime(
            searchRunnable, SEARCH_REQUEST_TOKEN, postTime
        )
    }

    private fun searchRequest(inputSearchText: String) {
        if (inputSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            searchSongsUseCase.execute(inputSearchText, onSuccess = { tracks ->
                renderState(SearchState.SearchContent(tracks))
                renderSavedTracks(tracks)
            }, onError = { errorStatus ->
                when (errorStatus) {
                    ErrorStatus.NOTHING_FOUND -> {
                        renderState(SearchState.Error(errorStatus))
                    }
                    ErrorStatus.NO_CONNECTION -> {
                        renderState(SearchState.Error(errorStatus))
                        latestSearchText = DEFAULT_TEXT
                    }
                }
            })
        }
    }

    fun addToListenHistory(track: Track) {
        addTrackToListenHistoryUseCase.execute(track)
    }

    fun clearListenHistory() {
        clearListenHistoryTracksUseCase.execute()
        renderState(SearchState.Empty)
    }

    fun clearSearchInput() {
        clearSearchResultTracksUseCase.execute()
        savedTracksLiveData.postValue(listOf())
        if (getIsListenHistoryTracksNotEmptyUseCase.execute()) {
            renderState(SearchState.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
        } else {
            renderState(SearchState.Empty)
        }
    }

    fun updateState() {
        if (getIsListenHistoryTracksNotEmptyUseCase.execute() && getIsSearchResultIsEmptyUseCase.execute()) {
            renderState(SearchState.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
        } else {
            renderState(SearchState.SearchContent(getSearchResultTracksUseCase.execute()))
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        clearSearchResultTracksUseCase.execute()
        super.onCleared()
    }

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val DEFAULT_TEXT = ""
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    addTrackToListenHistoryUseCase = App.addTrackToListenHistoryUseCase,
                    clearListenHistoryTracksUseCase = App.clearListenHistoryTracksUseCase,
                    clearSearchResultTracksUseCase = App.clearSearchResultTracksUseCase,
                    getIsListenHistoryTracksNotEmptyUseCase = App.getIsListenHistoryTracksNotEmptyUseCase,
                    getListenHistoryTracksUseCase = App.getListenHistoryTracksUseCase,
                    getSearchResultTracksUseCase = App.getSearchResultTracksUseCase,
                    getIsSearchResultIsEmptyUseCase = App.getIsSearchResultIsEmptyUseCase,
                    searchSongsUseCase = App.searchSongsUseCase,
                    addTracksToSearchResultUseCase = App.addTracksToSearchResultUseCase
                )
            }
        }
    }
}