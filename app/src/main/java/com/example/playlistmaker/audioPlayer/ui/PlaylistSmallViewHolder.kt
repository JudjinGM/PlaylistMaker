package com.example.playlistmaker.audioPlayer.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.databinding.PlaylistSmallViewBinding

class PlaylistSmallViewHolder(
    private val binding: PlaylistSmallViewBinding,
    private val onPlaylistClicked: ((PlaylistModel)) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: PlaylistModel) {
        Glide.with(itemView)
            .load(item.playlistCoverImage)
            .placeholder(R.drawable.no_album)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)))
            .into(binding.playlistCoverImageView)
        binding.playlistNameTextView.text = item.playlistName

        binding.trackCountTextView.text =
            binding.root.resources.getQuantityString(
                R.plurals.tracks_plural, item.tracks.size, item.tracks.size
            )

        itemView.setOnClickListener {
            onPlaylistClicked.invoke(item)
        }
    }
}