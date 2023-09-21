package com.example.playlistmaker.library.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.databinding.PlaylistViewBinding

class PlaylistViewHolder(
    private val binding: PlaylistViewBinding,
    private val onPlaylistClicked: (Long) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: PlaylistModel) {

        Glide.with(itemView)
            .load(item.playlistCoverImage)
            .placeholder(R.drawable.playlist_cover_rv)
            .apply(RequestOptions().override(1600, 1600))
            .centerInside()
            .into(binding.playlistCoverImageView)
        binding.playlistNameTextView.text = item.playlistName

        binding.tracksCountTextView.text =
            binding.root.resources.getQuantityString(
                R.plurals.tracks_plural, item.tracks.size, item.tracks.size
            )

        itemView.setOnClickListener {
            onPlaylistClicked.invoke(item.playlistId)
        }
    }
}