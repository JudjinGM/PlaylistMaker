package com.example.playlistmaker.audioPlayer.ui.model

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.search.domain.model.Track


sealed interface AudioPlayerState {
    data class InitState(val track: Track, val playlists: List<PlaylistModel>): AudioPlayerState

    data class TrackState(val isFavorite: Boolean) : AudioPlayerState
    sealed class PlayerState(open val progress: String) : AudioPlayerState {
        object Default : PlayerState( DEFAULT_TIME)

        object Prepared : PlayerState( DEFAULT_TIME)

        data class Playing(override val progress: String) : PlayerState(progress)

        data class Paused(override val progress: String) : PlayerState(progress)
        data class Error(val error: PlayerError) : PlayerState(DEFAULT_TIME)
    }

    sealed interface PlayListState : AudioPlayerState {
        object Success : PlayListState
        sealed interface Error : PlayListState {
            object AlreadyHaveTrack : Error
            object ErrorOccurred : Error
        }
    }
    sealed interface BottomSheetState : AudioPlayerState {
        object Show : BottomSheetState
        object Hide : BottomSheetState
    }

    companion object {
        private const val DEFAULT_TIME = "00:00"
    }
}