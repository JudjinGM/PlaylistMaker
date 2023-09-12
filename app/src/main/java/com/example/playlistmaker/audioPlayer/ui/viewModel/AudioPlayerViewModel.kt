package com.example.playlistmaker.audioPlayer.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.audioPlayer.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audioPlayer.domain.useCase.AddTrackToFavoritesUseCase
import com.example.playlistmaker.audioPlayer.domain.useCase.AddTrackToPlaylistUseCase
import com.example.playlistmaker.audioPlayer.domain.useCase.DeleteTrackFromFavoritesUseCase
import com.example.playlistmaker.audioPlayer.domain.useCase.IsConnectedToNetworkUseCase
import com.example.playlistmaker.audioPlayer.ui.model.AddPlaylistState
import com.example.playlistmaker.audioPlayer.ui.model.BottomSheetState
import com.example.playlistmaker.audioPlayer.ui.model.FavoriteState
import com.example.playlistmaker.audioPlayer.ui.model.PlayerError
import com.example.playlistmaker.audioPlayer.ui.model.PlayerState
import com.example.playlistmaker.audioPlayer.ui.model.PlaylistListState
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.library.domain.useCase.GetPlaylistListUseCase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val track: Track,
    private val mediaPlayerContract: MediaPlayerContract,
    private val isConnectedToNetworkUseCase: IsConnectedToNetworkUseCase,
    private val addTrackToFavoritesUseCase: AddTrackToFavoritesUseCase,
    private val deleteTrackFromFavoritesUseCase: DeleteTrackFromFavoritesUseCase,
    private val getPlaylistListUseCase: GetPlaylistListUseCase,
    private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase

) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())

    private val favoriteStateLiveData = MutableLiveData<FavoriteState>()

    private val bottomSheetStateLiveData = MutableLiveData<BottomSheetState>()

    private val toastErrorStateLiveData = SingleLiveEvent<PlayerError>()

    private val toastStateLiveData = SingleLiveEvent<AddPlaylistState>()

    private val playlistListState = MutableLiveData<PlaylistListState>()

    private var timerJob: Job? = null

    init {
        initMediaPlayer()

        if (track.isFavorite) {
            setFavoriteState(FavoriteState.Favorite)
        } else setFavoriteState(FavoriteState.NotFavorite)

        viewModelScope.launch {
            getPlaylistListUseCase.execute().collect {
                playlistListState.value = PlaylistListState.Success(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerContract.release()
    }

    fun observeErrorToastState(): LiveData<PlayerError> = toastErrorStateLiveData
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    fun observeFavoriteState(): LiveData<FavoriteState> = favoriteStateLiveData
    fun observePlaylistToastState(): LiveData<AddPlaylistState> = toastStateLiveData

    fun observeBottomSheetState(): LiveData<BottomSheetState> = bottomSheetStateLiveData

    fun observePlaylistListState(): LiveData<PlaylistListState> = playlistListState


    fun togglePlay() {
        when (playerStateLiveData.value) {
            is PlayerState.Default -> setToastState(PlayerError.NOT_READY)
            is PlayerState.Paused, is PlayerState.Prepared -> startPlayer()
            is PlayerState.Playing -> pausePlayer()
            else -> {
            }
        }
    }

    private fun setPlayerState(state: PlayerState) {
        playerStateLiveData.value = state
    }

    private fun setToastState(playerError: PlayerError) {
        toastErrorStateLiveData.value = playerError
    }

    private fun setFavoriteState(favoriteState: FavoriteState) {
        favoriteStateLiveData.value = favoriteState
    }

    private fun initMediaPlayer() {
        mediaPlayerContract.setOnPreparedListener {
            setPlayerState(PlayerState.Prepared())
        }
        mediaPlayerContract.setOnPlayListener {
            setPlayerState(PlayerState.Playing(getCurrentPosition()))
        }
        mediaPlayerContract.setOnPauseListener {
            setPlayerState(PlayerState.Paused(getCurrentPosition()))
        }
        mediaPlayerContract.setOnCompletionListener {
            setPlayerState(PlayerState.Prepared())
            stopTimer()
        }
        mediaPlayerContract.setOnErrorListener {
            setToastState(PlayerError.ERROR_OCCURRED)
        }

        if (isConnectedToNetworkUseCase.execute()) {
            mediaPlayerContract.initMediaPlayer(track.previewUrl)
        } else setToastState(PlayerError.NO_CONNECTION)
    }

    private fun startPlayer() {
        mediaPlayerContract.play()
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayerContract.pause()
        stopTimer()
    }

    fun onFavoriteClicked() {
        if (track.isFavorite) {
            track.isFavorite = false
            setFavoriteState(FavoriteState.NotFavorite)
            viewModelScope.launch {
                deleteTrackFromFavoritesUseCase.execute(track)
            }
        } else {
            track.isFavorite = true
            setFavoriteState(FavoriteState.Favorite)
            viewModelScope.launch {
                addTrackToFavoritesUseCase.execute(track)
            }
        }
    }

    fun onLibraryClicked() {
        bottomSheetStateLiveData.value = BottomSheetState.Show
    }

    fun addTrackToPlaylist(playlist: PlaylistModel) {

        var isPlaylistHaveTrack: Boolean = false

        playlist.tracks.forEach {
            if (it.trackId == track.trackId) {
                isPlaylistHaveTrack = true
            }
        }

        if (isPlaylistHaveTrack) {
            toastStateLiveData.value = AddPlaylistState.Error.AlreadyHaveTrack
        } else {
            bottomSheetStateLiveData.value = BottomSheetState.Hide

            viewModelScope.launch {
                addTrackToPlaylistUseCase.execute(playlist.playlistId, track)
            }
            toastStateLiveData.value = AddPlaylistState.Success
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerContract.isPlaying()) {
                delay(DELAY_MILLIS)
                setPlayerState(PlayerState.Playing(getCurrentPosition()))
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }


    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayerContract.getCurrentPosition()) ?: DEFAULT_TIME
    }

    companion object {
        private const val DELAY_MILLIS = 300L
        private const val DEFAULT_TIME = "00:00"
    }
}
