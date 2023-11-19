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
import com.example.playlistmaker.audioPlayer.ui.model.AudioPlayerState
import com.example.playlistmaker.audioPlayer.ui.model.PlayerError
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

    private val playerStateLiveData =
        MutableLiveData<AudioPlayerState>(AudioPlayerState.PlayerState.Default)


    private var timerJob: Job? = null
    private var isPrepared = false

    private var isFavorite = false

    fun observeAudioPlayerState(): LiveData<AudioPlayerState> = playerStateLiveData

    init {
        isFavorite = track.isFavorite
    }

    fun initState() {
        viewModelScope.launch {
            getPlaylistListUseCase.execute().collect {
                val currentState = getCurrentState()
                setState(AudioPlayerState.InitState(track, it))
                currentState?.let { setState(currentState) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerContract.release()
    }

    fun togglePlay() {
        when (playerStateLiveData.value) {
            is AudioPlayerState.PlayerState.Default -> {
                val currentState = getCurrentState()
                setState(AudioPlayerState.PlayerState.Error(PlayerError.NOT_READY))
                currentState?.let { setState(it) }
            }

            is AudioPlayerState.PlayerState.Paused, is AudioPlayerState.PlayerState.Prepared -> startPlayer()

            is AudioPlayerState.PlayerState.Playing -> pausePlayer()
            else -> {}
        }
    }

    private fun setState(state: AudioPlayerState) {
        playerStateLiveData.value = state
    }

    fun initMediaPlayer() {
        mediaPlayerContract.setOnPreparedListener {
            isPrepared = true
            setState(AudioPlayerState.PlayerState.Prepared)
        }
        mediaPlayerContract.setOnPlayListener {
            startTimer()
        }
        mediaPlayerContract.setOnPauseListener {
            setState(AudioPlayerState.PlayerState.Paused(getCurrentPosition()))
        }
        mediaPlayerContract.setOnCompletionListener {
            setState(AudioPlayerState.PlayerState.Prepared)
            stopTimer()
        }
        mediaPlayerContract.setOnErrorListener {
            setState(AudioPlayerState.PlayerState.Error(PlayerError.ERROR_OCCURRED))
        }

        if (isConnectedToNetworkUseCase.execute()) {
            mediaPlayerContract.initMediaPlayer(track.previewUrl)
        } else setState(AudioPlayerState.PlayerState.Error(PlayerError.NO_CONNECTION))
    }

    private fun startPlayer() {
        mediaPlayerContract.play()
        startTimer()
    }

    fun pausePlayer() {
        if (isPrepared) {
            mediaPlayerContract.pause()
        }
    }

    fun onFavoriteClicked() {
        if (isFavorite) {
            isFavorite = false
            viewModelScope.launch {
                deleteTrackFromFavoritesUseCase.execute(track)
            }
        } else {
            isFavorite = true
            viewModelScope.launch {
                addTrackToFavoritesUseCase.execute(track)
            }
        }
        val currentState = getCurrentState()
        setState(AudioPlayerState.TrackState(isFavorite))
        currentState?.let { setState(it) }
    }

    fun onLibraryClicked() {
        val currentState = getCurrentState()
        setState(AudioPlayerState.BottomSheetState.Show)
        currentState?.let { setState(it) }
    }

    fun addTrackToPlaylist(playlist: PlaylistModel) {

        val currentState = getCurrentState()
        var isPlaylistHaveTrack = false

        playlist.tracks.forEach {
            if (it.trackId == track.trackId) {
                isPlaylistHaveTrack = true
            }
        }

        if (isPlaylistHaveTrack) {
            setState(AudioPlayerState.PlayListState.Error.AlreadyHaveTrack)
        } else {
            setState(AudioPlayerState.BottomSheetState.Hide)

            viewModelScope.launch {
                addTrackToPlaylistUseCase.execute(playlist.playlistId, track)
            }
            setState(AudioPlayerState.PlayListState.Success)
        }
        currentState?.let { setState(it) }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerContract.isPlaying()) {
                setState(AudioPlayerState.PlayerState.Playing(getCurrentPosition()))
                delay(DELAY_MILLIS)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }


    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayerContract.getCurrentPosition())
    }

    private fun getCurrentState(): AudioPlayerState? {
        return when (playerStateLiveData.value) {
            is AudioPlayerState.InitState -> (playerStateLiveData.value as AudioPlayerState.InitState).copy()
            AudioPlayerState.PlayListState.Error.AlreadyHaveTrack -> (playerStateLiveData.value as AudioPlayerState.PlayListState.Error.AlreadyHaveTrack)
            AudioPlayerState.PlayListState.Error.ErrorOccurred -> (playerStateLiveData.value as AudioPlayerState.PlayListState.Error.ErrorOccurred)
            AudioPlayerState.PlayListState.Success -> (playerStateLiveData.value as AudioPlayerState.PlayListState.Success)
            AudioPlayerState.BottomSheetState.Hide -> (playerStateLiveData.value as AudioPlayerState.BottomSheetState.Hide)
            AudioPlayerState.BottomSheetState.Show -> (playerStateLiveData.value as AudioPlayerState.BottomSheetState.Show)
            AudioPlayerState.PlayerState.Default -> (playerStateLiveData.value as AudioPlayerState.PlayerState.Default)
            is AudioPlayerState.PlayerState.Error -> (playerStateLiveData.value as AudioPlayerState.PlayerState.Error).copy()
            is AudioPlayerState.PlayerState.Paused -> (playerStateLiveData.value as AudioPlayerState.PlayerState.Paused).copy()
            is AudioPlayerState.PlayerState.Playing -> (playerStateLiveData.value as AudioPlayerState.PlayerState.Playing).copy()
            AudioPlayerState.PlayerState.Prepared -> (playerStateLiveData.value as AudioPlayerState.PlayerState.Prepared)
            is AudioPlayerState.TrackState -> (playerStateLiveData.value as AudioPlayerState.TrackState).copy()
            null -> null
        }
    }

    companion object {
        private const val DELAY_MILLIS = 300L
    }
}
