package com.example.playlistmaker.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.databinding.PlaylistViewBinding

class PlaylistAdapter :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists: List<PlaylistModel> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistViewBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.onBind(playlists[position])
    }

    fun updateAdapter(newPlaylists: List<PlaylistModel>) {
        val diffResult = getDiffResult(playlists, newPlaylists)
        playlists = newPlaylists
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getDiffResult(
        oldList: List<PlaylistModel>,
        newList: List<PlaylistModel>
    ): DiffUtil.DiffResult {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].playlistId == newList[newItemPosition].playlistId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })
        return diffResult
    }
}