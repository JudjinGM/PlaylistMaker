package com.example.playlistmaker.audioPlayer.ui.model

sealed interface BottomSheetState {
    object Show : BottomSheetState
    object Hide : BottomSheetState
}