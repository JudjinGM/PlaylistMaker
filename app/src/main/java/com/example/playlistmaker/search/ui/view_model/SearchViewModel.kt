package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.model.ErrorStatus
import com.example.playlistmaker.search.domain.model.SearchState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.use_case.AddTrackToListenHistoryUseCase
import com.example.playlistmaker.search.domain.use_case.AddTracksToSearchResultUseCase
import com.example.playlistmaker.search.domain.use_case.ClearListenHistoryTracksUseCase
import com.example.playlistmaker.search.domain.use_case.ClearSearchResultTracksUseCase
import com.example.playlistmaker.search.domain.use_case.GetIsListenHistoryTracksNotEmptyUseCase
import com.example.playlistmaker.search.domain.use_case.GetIsSearchResultIsEmptyUseCase
import com.example.playlistmaker.search.domain.use_case.GetListenHistoryTracksUseCase
import com.example.playlistmaker.search.domain.use_case.GetSearchResultTracksUseCase
import com.example.playlistmaker.search.domain.use_case.SearchSongsUseCase
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

    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<SearchState>()
    private val savedTracksLiveData = MutableLiveData<List<Track>>()

    private val handler = Handler(Looper.getMainLooper())


    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        clearSearchResultTracksUseCase.execute()
        super.onCleared()
    }

    fun init(savedTracks: SavedTracks?) {
        val tracks = savedTracks?.tracks?.toList() ?: listOf()
        if (tracks.isNotEmpty()) {
            postSavedTracks(tracks)
            addTracksToSearchResultUseCase.execute(tracks)
            postState(SearchState.Success.SearchContent(getSearchResultTracksUseCase.execute()))
        } else {
            if (getIsListenHistoryTracksNotEmptyUseCase.execute()) {
                postState(SearchState.Success.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
            } else postState(SearchState.Success.Empty)
        }
    }

    fun observeState(): LiveData<SearchState> = stateLiveData

    fun observeSavedTracks(): LiveData<List<Track>> = savedTracksLiveData

    private fun postSavedTracks(tracks: List<Track>) {
        savedTracksLiveData.postValue(tracks)
    }

    private fun postState(state: SearchState) {
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
            postState(SearchState.Loading)

            searchSongsUseCase.execute(inputSearchText, onSuccess = { tracks ->
                postState(SearchState.Success.SearchContent(tracks))
                postSavedTracks(tracks)
            }, onError = { errorStatus ->
                when (errorStatus) {
                    ErrorStatus.NOTHING_FOUND -> {
                        postState(SearchState.Error(errorStatus))
                    }

                    ErrorStatus.NO_CONNECTION -> {
                        postState(SearchState.Error(errorStatus))
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
        postState(SearchState.Success.Empty)
    }

    fun clearSearchInput() {
        clearSearchResultTracksUseCase.execute()
        savedTracksLiveData.postValue(emptyList())
        if (getIsListenHistoryTracksNotEmptyUseCase.execute()) {
            postState(SearchState.Success.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
        } else {
            postState(SearchState.Success.Empty)
        }
    }

    fun updateState() {
        if (getIsListenHistoryTracksNotEmptyUseCase.execute() && getIsSearchResultIsEmptyUseCase.execute()) {
            postState(SearchState.Success.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
        } else {
            postState(SearchState.Success.SearchContent(getSearchResultTracksUseCase.execute()))
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val DEFAULT_TEXT = ""
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}