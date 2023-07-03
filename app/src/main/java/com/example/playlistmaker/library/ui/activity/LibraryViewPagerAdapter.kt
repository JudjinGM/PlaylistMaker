package com.example.playlistmaker.library.ui.activity

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.library.ui.activity.fragments.FavoritesFragment
import com.example.playlistmaker.library.ui.activity.fragments.PlaylistsFragment

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int) =
        if (position == 0) FavoritesFragment.newInstance() else PlaylistsFragment.newInstance()
}
