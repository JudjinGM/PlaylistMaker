package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.model.ErrorStatusDomain
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
import com.example.playlistmaker.search.ui.model.ErrorStatusUi
import com.example.playlistmaker.search.ui.model.SavedTracks
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

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

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            searchRequest(it)
        }

    override fun onCleared() {
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

        tracksSearchDebounce(changedText)
    }

    private fun searchRequest(inputSearchText: String) {
        if (inputSearchText.isNotEmpty()) {
            postState(SearchState.Loading)


            viewModelScope.launch {
                searchSongsUseCase.execute(inputSearchText).collect {
                    processResult(it.first, it.second)
                }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorStatus: ErrorStatusDomain?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorStatus != null -> {
                when (errorStatus) {

                    ErrorStatusDomain.NO_CONNECTION -> {
                        postState(SearchState.Error(ErrorStatusUi.NO_CONNECTION))
                        latestSearchText = DEFAULT_TEXT
                    }
                }
            }

            tracks.isEmpty() -> postState(SearchState.Error(ErrorStatusUi.NOTHING_FOUND))

            else -> {
                postState(SearchState.Success.SearchContent(tracks))
                postSavedTracks(tracks)
            }
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
    }
}