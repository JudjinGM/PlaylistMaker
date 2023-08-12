package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.repository.SearchRepository

interface UpdateSearchTracksIsFavoriteUseCase {
    fun execute(favoriteTracksId: List<Long>)

    class Base(private val searchRepository: SearchRepository) :
        UpdateSearchTracksIsFavoriteUseCase {
        override fun execute(favoriteTracksId: List<Long>) {
            searchRepository.updateSearchResultFavorite(favoriteTracksId)
        }
    }
}