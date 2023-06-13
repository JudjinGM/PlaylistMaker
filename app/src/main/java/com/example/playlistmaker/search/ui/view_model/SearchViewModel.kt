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

class SearchViewModel(
    private val addTracksToListenHistoryUseCase: AddTracksToListenHistoryUseCase,
    private val clearListenHistoryTracksUseCase: ClearListenHistoryTracksUseCase,
    private val clearSearchTracksUseCase: ClearSearchTracksUseCase,
    private val getIsListenHistoryTracksNotEmptyUseCase: GetIsListenHistoryTracksNotEmptyUseCase,
    private val getListenHistoryTracksUseCase: GetListenHistoryTracksUseCase,
    private val getSearchResultTracksUseCase: GetSearchResultTracksUseCase,
    private val getIsSearchResultIsEmptyUseCase: GetIsSearchResultIsEmptyUseCase,
    private val searchSongsUseCase: SearchSongsUseCase
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    init {
        if (getIsListenHistoryTracksNotEmptyUseCase.execute()) {
            renderState(SearchState.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
        } else renderState(SearchState.Empty)
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
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    private fun searchRequest(inputSearchText: String) {
        if (inputSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            searchSongsUseCase.execute(inputSearchText, onSuccess = { tracks ->
                renderState(SearchState.SearchContent(tracks))
            }, onError = { errorStatus ->
                when (errorStatus) {
                    ErrorStatus.NOTHING_FOUND -> {
                        clearSearchTracksUseCase.execute()
                        renderState(SearchState.Error(errorStatus))
                    }
                    ErrorStatus.NO_CONNECTION -> {
                        clearSearchTracksUseCase.execute()
                        renderState(SearchState.Error(errorStatus))
                    }
                }
            })
        }
    }

    fun addToListenHistory(track: Track) {
        addTracksToListenHistoryUseCase.execute(track)
    }

    fun clearListenHistory() {
        clearListenHistoryTracksUseCase.execute()
        renderState(SearchState.Empty)
    }

    fun clearSearchInput() {
        clearSearchTracksUseCase.execute()
    }

    fun updateState() {
        if (getIsListenHistoryTracksNotEmptyUseCase.execute() && getIsSearchResultIsEmptyUseCase.execute()) {
            renderState(SearchState.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
        } else renderState(SearchState.SearchContent(getSearchResultTracksUseCase.execute()))
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        clearSearchTracksUseCase.execute()
        super.onCleared()
    }

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    addTracksToListenHistoryUseCase = App.addTracksToListenHistoryUseCase,
                    clearListenHistoryTracksUseCase = App.clearListenHistoryTracksUseCase,
                    clearSearchTracksUseCase = App.clearSearchTracksUseCase,
                    getIsListenHistoryTracksNotEmptyUseCase = App.getIsListenHistoryTracksNotEmptyUseCase,
                    getListenHistoryTracksUseCase = App.getListenHistoryTracksUseCase,
                    getSearchResultTracksUseCase = App.getSearchResultTracksUseCase,
                    getIsSearchResultIsEmptyUseCase = App.getIsSearchResultIsEmptyUseCase,
                    searchSongsUseCase = App.searchSongsUseCase
                )
            }
        }
    }
}