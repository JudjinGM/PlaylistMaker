package com.example.playlistmaker.library.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.databinding.PlaylistViewBinding

class PlaylistViewHolder(private val binding: PlaylistViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: PlaylistModel) {

        Glide.with(itemView)
            .load(item.playlistCoverImage)
            .placeholder(R.drawable.playlist_cover_rv)
            .apply(RequestOptions().override(1600, 1600))
            .centerInside()
            .into(binding.playlistCoverImageView)
        binding.playlistNameTextView.text = item.playlistName

        if (item.tracks.size == 1) {
            binding.tracksCountTextView.text =
                binding.root.context.getString(R.string.count_track, item.tracks.size)
        } else {
            binding.tracksCountTextView.text =
                binding.root.context.getString(R.string.count_tracks, item.tracks.size)
        }
    }
}