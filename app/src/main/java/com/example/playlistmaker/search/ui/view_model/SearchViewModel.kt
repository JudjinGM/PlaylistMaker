package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.model.ErrorStatusDomain
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
import com.example.playlistmaker.search.ui.model.SearchState
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val addTrackToListenHistoryUseCase: AddTrackToListenHistoryUseCase,
    private val clearListenHistoryTracksUseCase: ClearListenHistoryTracksUseCase,
    private val clearSearchResultTracksUseCase: ClearSearchResultTracksUseCase,
    private val getIsListenHistoryTracksNotEmptyUseCase: GetIsListenHistoryTracksNotEmptyUseCase,
    private val getListenHistoryTracksUseCase: GetListenHistoryTracksUseCase,
    private val getSearchResultTracksUseCase: GetSearchResultTracksUseCase,
    private val getIsSearchResultIsEmptyUseCase: GetIsSearchResultIsEmptyUseCase,
    private val searchSongsUseCase: SearchSongsUseCase,
    private val addTracksToSearchResultUseCase: AddTracksToSearchResultUseCase,
) : ViewModel() {

    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<SearchState>()

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            searchRequest(it)
        }

    init {
        val savedTracks = savedStateHandle.get<SavedTracks?>(SAVED_SEARCH_TRACKS)
        val tracks = savedTracks?.tracks?.toList() ?: listOf()
        addTracksToSearchResultUseCase.execute(tracks)
    }

    override fun onCleared() {
        clearSearchResultTracksUseCase.execute()
        super.onCleared()
    }

    fun observeState(): LiveData<SearchState> = stateLiveData

    private fun setState(state: SearchState) {
        stateLiveData.value = state
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
            setState(SearchState.Loading)

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
                        setState(SearchState.Error(ErrorStatusUi.NO_CONNECTION))
                        latestSearchText = DEFAULT_TEXT
                    }
                }
            }

            tracks.isEmpty() -> setState(SearchState.Error(ErrorStatusUi.NOTHING_FOUND))

            else -> {
                addTracksToSearchResultUseCase.execute(tracks)
                savedStateHandle[SAVED_SEARCH_TRACKS] = SavedTracks(ArrayList(tracks))
                setState(SearchState.Success.SearchContent(tracks))
            }
        }
    }

    fun addToListenHistory(track: Track) {
        addTrackToListenHistoryUseCase.execute(track)
    }

    fun clearListenHistory() {
        clearListenHistoryTracksUseCase.execute()
        updateState()
    }

    fun clearSearchInput() {
        clearSearchResultTracksUseCase.execute()
        savedStateHandle[SAVED_SEARCH_TRACKS] = SavedTracks(arrayListOf())
        updateState()
    }

    fun updateState() {
        if (getIsListenHistoryTracksNotEmptyUseCase.execute() && getIsSearchResultIsEmptyUseCase.execute()) {
            setState(SearchState.Loading)
            viewModelScope.launch {
                setState(SearchState.Success.ListenHistoryContent(getListenHistoryTracksUseCase.execute()))
            }
        } else {
            viewModelScope.launch {
                setState(SearchState.Success.SearchContent(getSearchResultTracksUseCase.execute()))
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val DEFAULT_TEXT = ""
        private const val SAVED_SEARCH_TRACKS = "SAVED_SEARCH_TRACKS"
    }
}