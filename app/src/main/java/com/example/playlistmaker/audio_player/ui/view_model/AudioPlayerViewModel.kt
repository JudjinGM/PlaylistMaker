package com.example.playlistmaker.audio_player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.audio_player.domain.use_case.IsConnectedToNetworkUseCase
import com.example.playlistmaker.audio_player.domain.use_case.MediaPlayerControlInteractor
import com.example.playlistmaker.audio_player.ui.model.PlayerError
import com.example.playlistmaker.audio_player.ui.model.PlayerState
import com.example.playlistmaker.audio_player.ui.model.PlayerStatus
import com.example.playlistmaker.search.domain.model.Track

class AudioPlayerViewModel(
    track: Track,
    private val mediaPlayerControlInteractor: MediaPlayerControlInteractor,
    isConnectedToNetworkUseCase: IsConnectedToNetworkUseCase
) : ViewModel() {

    private var playerStatus = PlayerStatus.STATE_DEFAULT

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    private val toastSateLiveData = SingleLiveEvent<PlayerError>()
    private val timeLiveData = MutableLiveData<Long>()

    private var mainTreadHandler: Handler = Handler(Looper.getMainLooper())
    private var trackTimeUpdateRunnable: Runnable? = null

    init {
        mediaPlayerControlInteractor.setOnPreparedListener {
            playerStatus = PlayerStatus.STATE_PREPARED
        }
        mediaPlayerControlInteractor.setOnPlayListener {
            playerStatus = PlayerStatus.STATE_PLAYING
        }

        mediaPlayerControlInteractor.setOnPauseListener {
            playerStatus = PlayerStatus.STATE_PAUSED
        }

        mediaPlayerControlInteractor.setOnStopListener {
            playerStatus = PlayerStatus.STATE_PREPARED
        }

        mediaPlayerControlInteractor.setOnCompletionListener {
            playerStatus = PlayerStatus.STATE_PREPARED
            playerStateToPlayerStatusUpdate()
            timeStateUpdate()
        }

        mediaPlayerControlInteractor.setOnErrorListener {
            playerStatus = PlayerStatus.STATE_ERROR
        }

        if (isConnectedToNetworkUseCase.execute()) {
            mediaPlayerControlInteractor.initPlayer(track.previewUrl)
        } else playerStatus = PlayerStatus.STATE_NETWORK_ERROR
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerControlInteractor.releasePlayer()
        trackTimeUpdateRunnable?.let { mainTreadHandler.removeCallbacks(it) }
        trackTimeUpdateRunnable = null
    }

    fun observeToastState(): LiveData<PlayerError> = toastSateLiveData
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData
    fun observeTime(): LiveData<Long> = timeLiveData

    private fun postPlayerState(state: PlayerState) {
        playerStateLiveData.postValue(state)
    }

    private fun postTimeState(time: Long) {
        timeLiveData.postValue(time)
    }

    private fun postToastState(playerError: PlayerError) {
        toastSateLiveData.postValue(playerError)
    }

    fun togglePlay() {
        playbackControl(playerStatus)
        playerStateToPlayerStatusUpdate()
        timeStateUpdate()
    }

    private fun playbackControl(playerStatus: PlayerStatus) {
        when (playerStatus) {
            PlayerStatus.STATE_PLAYING -> {
                mediaPlayerControlInteractor.pausePlayer()
            }
            PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED -> {
                mediaPlayerControlInteractor.playPlayer()
            }
            else -> {
            }
        }
    }

    private fun playerStateToPlayerStatusUpdate() = when (playerStatus) {
        PlayerStatus.STATE_PREPARED -> postPlayerState(PlayerState.Pause)
        PlayerStatus.STATE_PLAYING -> postPlayerState(PlayerState.Play)
        PlayerStatus.STATE_PAUSED -> postPlayerState(PlayerState.Pause)
        PlayerStatus.STATE_DEFAULT -> postToastState(PlayerError.NOT_READY)
        PlayerStatus.STATE_ERROR -> postToastState(PlayerError.ERROR_OCCURRED)
        PlayerStatus.STATE_NETWORK_ERROR -> postToastState(PlayerError.NO_CONNECTION)
    }

    private fun timeStateUpdate() {
        var runnable = trackTimeUpdateRunnable
        if (runnable?.let { mainTreadHandler.hasCallbacks(it) } == true) {
            mainTreadHandler.removeCallbacks(runnable)
        }
        runnable = getTrackTimeUpdateRunnable()
        mainTreadHandler.post(runnable)
        trackTimeUpdateRunnable = runnable
    }

    private fun getTrackTimeUpdateRunnable(): Runnable {
        val result = object : Runnable {
            override fun run() {
                when (playerStatus) {
                    PlayerStatus.STATE_DEFAULT, PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_ERROR, PlayerStatus.STATE_NETWORK_ERROR -> {
                        mainTreadHandler.removeCallbacks(this)
                        postTimeState(DEFAULT_TIME)
                    }
                    PlayerStatus.STATE_PLAYING -> {
                        mainTreadHandler.postDelayed(this, DELAY_MILLIS)
                        postTimeState(mediaPlayerControlInteractor.getMediaPlayerCurrentPosition())
                    }
                    PlayerStatus.STATE_PAUSED -> {
                        postTimeState(mediaPlayerControlInteractor.getMediaPlayerCurrentPosition())
                    }
                }
            }
        }
        return result
    }

    fun pausePlayer() {
        if (playerStatus == PlayerStatus.STATE_PLAYING || playerStatus == PlayerStatus.STATE_PAUSED) {
            mediaPlayerControlInteractor.pausePlayer()
            playerStateToPlayerStatusUpdate()
            timeStateUpdate()
        }
    }

    companion object {
        private const val DELAY_MILLIS = 500L
        private const val DEFAULT_TIME = 0L
    }
}
