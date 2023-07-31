package com.example.playlistmaker.audio_player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audio_player.domain.use_case.IsConnectedToNetworkUseCase
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
    private val isConnectedToNetworkUseCase: IsConnectedToNetworkUseCase
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())

    private val toastStateLiveData = SingleLiveEvent<PlayerError>()

    private var timerJob: Job? = null

    init {
        initMediaPlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerContract.release()
    }

    fun observeToastState(): LiveData<PlayerError> = toastStateLiveData
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData


    fun togglePlay() {
        when (playerStateLiveData.value) {
            is PlayerState.Default -> postToastState(PlayerError.NOT_READY)
            is PlayerState.Paused, is PlayerState.Prepared -> startPlayer()
            is PlayerState.Playing -> pausePlayer()
            else -> {
            }
        }
    }

    private fun postPlayerState(state: PlayerState) {
        playerStateLiveData.postValue(state)
    }

    private fun postToastState(playerError: PlayerError) {
        toastStateLiveData.postValue(playerError)
    }

    private fun initMediaPlayer() {
        mediaPlayerContract.setOnPreparedListener {
            postPlayerState(PlayerState.Prepared())
        }
        mediaPlayerContract.setOnPlayListener {
            postPlayerState(PlayerState.Playing(getCurrentPosition()))
        }
        mediaPlayerContract.setOnPauseListener {
            postPlayerState(PlayerState.Paused(getCurrentPosition()))
        }
        mediaPlayerContract.setOnCompletionListener {
            postPlayerState(PlayerState.Prepared())
            stopTimer()
        }
        mediaPlayerContract.setOnErrorListener {
            postToastState(PlayerError.ERROR_OCCURRED)
        }

        if (isConnectedToNetworkUseCase.execute()) {
            mediaPlayerContract.initMediaPlayer(track.previewUrl)
        } else postToastState(PlayerError.NO_CONNECTION)
    }

    private fun startPlayer() {
        mediaPlayerContract.play()
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayerContract.pause()
        stopTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerContract.isPlaying()) {
                delay(DELAY_MILLIS)
                postPlayerState(PlayerState.Playing(getCurrentPosition()))
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
