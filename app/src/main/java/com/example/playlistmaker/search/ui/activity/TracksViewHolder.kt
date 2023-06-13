package com.example.playlistmaker.search.ui.activity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TracksViewBinding
import com.example.playlistmaker.search.domain.model.Track

class TracksViewHolder(
    private val binding: TracksViewBinding,
    private val onTrackClicked: ((Track) -> Unit)
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Track) {
        Glide.with(itemView).load(item.artworkUrl100).placeholder(R.drawable.no_album)
            .centerInside()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)))
            .into(binding.albumCoverImageView)
        binding.songNameTextView.text = item.trackName
        binding.artistNameTextView.text = item.artistName
        binding.songTimeStampTextView.text = item.trackTimeMillis

        itemView.setOnClickListener {
            onTrackClicked.invoke(item)
        }
    }
}