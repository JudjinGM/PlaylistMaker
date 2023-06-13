package com.example.playlistmaker.audio_player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.audio_player.data.impl.MediaPlayerImpl
import com.example.playlistmaker.audio_player.domain.model.PlayerError
import com.example.playlistmaker.audio_player.domain.model.PlayerState
import com.example.playlistmaker.audio_player.domain.model.PlayerStatus
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audio_player.domain.use_case.*
import com.example.playlistmaker.search.domain.model.Track

class AudioPlayerViewModel(track: Track) : ViewModel() {

    private var mainTreadHandler: Handler = Handler(Looper.getMainLooper())
    private var trackTimeUpdateRunnable: Runnable? = null

    private var playerStatus = PlayerStatus.STATE_DEFAULT
    private var mediaPlayer: MediaPlayerContract = MediaPlayerImpl()

    private val mediaPlayerInitUseCase = MediaPlayerInitUseCase.Base(
        mediaPlayer
    ) { playerStatus -> this.playerStatus = playerStatus }

    private val mediaPlayerControlUseCase =
        MediaPlayerControlUseCase.Base(mediaPlayer) { playerStatus ->
            this.playerStatus = playerStatus
        }

    private val mediaPlayerPlaybackControlUseCase = MediaPlayerPlaybackControlUseCase.Base(
        mediaPlayerControlUseCase,
    ) { playerStatus ->
        this.playerStatus = playerStatus
        playerStateToPlayerStatusUpdate()
    }

    private val getMediaPlayerCurrentPositionUseCase =
        GetMediaPlayerCurrentPositionUseCase.Base(mediaPlayer)

    init {
        mediaPlayer.setOnCompletionListener {
            playerStatus = PlayerStatus.STATE_PREPARED
            playerStateToPlayerStatusUpdate()
            timeStateUpdate()
        }
        mediaPlayerInitUseCase.initPlayer(track.previewUrl)
    }

    private val stateLiveData = MutableLiveData<PlayerState>()

    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val timeLiveData = MutableLiveData<Long>()

    fun observeTime(): LiveData<Long> = timeLiveData

    fun pausePlayer() {
        if (playerStatus == PlayerStatus.STATE_PLAYING || playerStatus == PlayerStatus.STATE_PAUSED) {
            mediaPlayerControlUseCase.pausePlayer()
            playerStateToPlayerStatusUpdate()
            timeStateUpdate()
        }
    }

    private fun renderPlayerState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    private fun renderTimeState(time: Long) {
        timeLiveData.postValue(time)
    }

    fun togglePlay() {
        mediaPlayerPlaybackControlUseCase.execute(playerStatus)
        playerStateToPlayerStatusUpdate()
        timeStateUpdate()
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
                    PlayerStatus.STATE_DEFAULT, PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_ERROR -> {
                        mainTreadHandler.removeCallbacks(this)
                        renderTimeState(DEFAULT_TIME)
                    }
                    PlayerStatus.STATE_PLAYING -> {
                        mainTreadHandler.postDelayed(this, DELAY_MILLIS)
                        renderTimeState(getMediaPlayerCurrentPositionUseCase.execute())
                    }
                    PlayerStatus.STATE_PAUSED -> {
                        renderTimeState(getMediaPlayerCurrentPositionUseCase.execute())
                    }
                }
            }
        }
        return result
    }

    private fun playerStateToPlayerStatusUpdate() = when (playerStatus) {
        PlayerStatus.STATE_PREPARED -> renderPlayerState(PlayerState.Pause)
        PlayerStatus.STATE_PLAYING -> renderPlayerState(PlayerState.Play)
        PlayerStatus.STATE_PAUSED -> renderPlayerState(PlayerState.Pause)
        PlayerStatus.STATE_DEFAULT -> renderPlayerState(PlayerState.Error(PlayerError.NOT_READY))
        PlayerStatus.STATE_ERROR -> renderPlayerState(PlayerState.Error(PlayerError.ERROR_OCCURRED))
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerControlUseCase.releasePlayer()
        trackTimeUpdateRunnable?.let { mainTreadHandler.removeCallbacks(it) }
        trackTimeUpdateRunnable = null
    }

    companion object {
        private const val DELAY_MILLIS = 500L
        private const val DEFAULT_TIME = 0L

        fun getViewModelFactory(track: Track): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    AudioPlayerViewModel(track)
                }
            }
    }
}