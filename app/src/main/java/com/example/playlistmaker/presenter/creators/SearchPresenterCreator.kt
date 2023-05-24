package com.example.playlistmaker.presenter.creators

import com.example.playlistmaker.presenter.SearchPresenter
import com.example.playlistmaker.presenter.SearchView

class SearchPresenterCreator() {
    fun createPresenter(view: SearchView):SearchPresenter{
        return SearchPresenter(view)
    }
}