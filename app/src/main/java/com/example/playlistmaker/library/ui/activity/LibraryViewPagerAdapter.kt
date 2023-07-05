package com.example.playlistmaker.library.ui.activity

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.library.ui.activity.fragments.FavoritesFragment
import com.example.playlistmaker.library.ui.activity.fragments.PlaylistsFragment
import com.example.playlistmaker.library.ui.activity.model.MediaFragmentType

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return NUMBER_OF_PAGES
    }

    override fun createFragment(position: Int) =
        when (MediaFragmentType.values()[position]) {
            MediaFragmentType.FAVORITES -> FavoritesFragment.newInstance()
            MediaFragmentType.PLAYLIST -> PlaylistsFragment.newInstance()
        }

    companion object {
        const val NUMBER_OF_PAGES = 2
    }
}
