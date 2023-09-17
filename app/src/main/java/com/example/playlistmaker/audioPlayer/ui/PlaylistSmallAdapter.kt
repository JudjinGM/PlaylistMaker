package com.example.playlistmaker.audioPlayer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.databinding.PlaylistSmallViewBinding

class PlaylistSmallAdapter(var onPlaylistClicked: (PlaylistModel) -> Unit = { }) :
    RecyclerView.Adapter<PlaylistSmallViewHolder>() {
    var playlists: List<PlaylistModel> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSmallViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistSmallViewHolder(
            PlaylistSmallViewBinding.inflate(
                layoutInflater,
                parent,
                false
            ), onPlaylistClicked
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistSmallViewHolder, position: Int) {
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