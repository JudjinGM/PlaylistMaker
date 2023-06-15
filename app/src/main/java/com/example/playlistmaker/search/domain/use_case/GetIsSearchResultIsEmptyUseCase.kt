package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.repository.SearchRepository

interface GetIsSearchResultIsEmptyUseCase {
    fun execute(): Boolean

    class Base(private val searchRepository: SearchRepository) : GetIsSearchResultIsEmptyUseCase {
        override fun execute(): Boolean {
            return searchRepository.getSearchTracks().isEmpty()
        }
    }
}