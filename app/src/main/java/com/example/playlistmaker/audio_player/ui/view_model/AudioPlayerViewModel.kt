package com.example.playlistmaker.audio_player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audio_player.domain.use_case.AddTrackToFavoritesUseCase
import com.example.playlistmaker.audio_player.domain.use_case.DeleteTrackFromFavoritesUseCase
import com.example.playlistmaker.audio_player.domain.use_case.IsConnectedToNetworkUseCase
import com.example.playlistmaker.audio_player.ui.model.FavoriteState
import com.example.playlistmaker.audio_player.ui.model.PlayerError
import com.example.playlistmaker.audio_player.ui.model.PlayerState
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


) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    private val favoriteStateLiveData = MutableLiveData<FavoriteState>()

    private val toastStateLiveData = SingleLiveEvent<PlayerError>()

    private var timerJob: Job? = null

    init {
        initMediaPlayer()

        if (track.isFavorite) {
            setFavoriteState(FavoriteState.Favorite)
        } else setFavoriteState(FavoriteState.NotFavorite)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerContract.release()
    }

    fun observeToastState(): LiveData<PlayerError> = toastStateLiveData
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    fun observeFavoriteState(): LiveData<FavoriteState> = favoriteStateLiveData


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
        toastStateLiveData.value = playerError
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
