package com.example.playlistmaker.presenter.creators

import com.example.playlistmaker.presenter.search.SearchPresenter
import com.example.playlistmaker.presenter.search.SearchView

class SearchPresenterCreator() {
    fun createPresenter(view: SearchView): SearchPresenter {
        return SearchPresenter(view)
    }
}