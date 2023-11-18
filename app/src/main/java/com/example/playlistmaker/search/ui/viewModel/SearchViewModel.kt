package com.example.playlistmaker.search.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.model.ErrorStatusDomain
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.useCase.AddTrackToListenHistoryUseCase
import com.example.playlistmaker.search.domain.useCase.AddTracksToSearchResultUseCase
import com.example.playlistmaker.search.domain.useCase.ClearListenHistoryTracksUseCase
import com.example.playlistmaker.search.domain.useCase.ClearSearchResultTracksUseCase
import com.example.playlistmaker.search.domain.useCase.GetIsListenHistoryTracksNotEmptyUseCase
import com.example.playlistmaker.search.domain.useCase.GetIsSearchResultIsEmptyUseCase
import com.example.playlistmaker.search.domain.useCase.GetListenHistoryTracksUseCase
import com.example.playlistmaker.search.domain.useCase.GetSearchResultTracksUseCase
import com.example.playlistmaker.search.domain.useCase.SearchSongsUseCase
import com.example.playlistmaker.search.ui.model.ErrorStatusUi
import com.example.playlistmaker.search.ui.model.SavedTracks
import com.example.playlistmaker.search.ui.model.SearchState
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var isClickAllowed = true

    private var job: Job? = null

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, _ ->
            setState(SearchState.Error(ErrorStatusUi.NO_CONNECTION))
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

    fun refreshSearchDebounced() {
        if (isClickDebounce()) {
            latestSearchText?.let { searchDebounced(it) }
        }
    }

    private fun searchRequest(inputSearchText: String) {
        if (inputSearchText.isNotEmpty()) {
            setState(SearchState.Loading)

            job = viewModelScope.launch(coroutineExceptionHandler) {
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

    fun clearListenHistory() {
        clearListenHistoryTracksUseCase.execute()
        updateState()
    }

    fun clearSearchInput() {
        job?.cancel()
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

    fun onTrackClicked(track: Track) {
        if (isClickDebounce()) {
            addTrackToListenHistoryUseCase.execute(track)
            val currentState: SearchState? = getCurrentState()
            setState(SearchState.NavigateToPlayer(track))
            currentState?.let { setState(it) }
        }
    }

    private fun getCurrentState(): SearchState? {
        val currentState = when (stateLiveData.value) {
            is SearchState.Error -> (stateLiveData.value as SearchState.Error).copy()
            SearchState.Loading -> (stateLiveData.value as SearchState.Loading)
            SearchState.Success.Empty -> (stateLiveData.value as SearchState.Success.Empty)
            is SearchState.Success.ListenHistoryContent -> (stateLiveData.value as SearchState.Success.ListenHistoryContent).copy()
            is SearchState.Success.SearchContent -> (stateLiveData.value as SearchState.Success.SearchContent).copy()
            is SearchState.NavigateToPlayer -> (stateLiveData.value as SearchState.NavigateToPlayer).copy()
            null -> null
        }
        return currentState
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
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val DEFAULT_TEXT = ""
        private const val SAVED_SEARCH_TRACKS = "SAVED_SEARCH_TRACKS"
    }
}