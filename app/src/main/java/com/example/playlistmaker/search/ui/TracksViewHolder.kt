package com.example.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TracksViewBinding
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(
    private val binding: TracksViewBinding,
    private val onTrackClicked: ((Track) -> Unit),
    private val onTrackClickedLong: ((Track) -> Unit)
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Track) {
        Glide.with(itemView).load(item.artworkUrl60).placeholder(R.drawable.no_album)
            .centerInside()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)))
            .into(binding.albumCoverImageView)
        binding.songNameTextView.text = item.trackName
        binding.artistNameTextView.text = item.artistName
        binding.songTimeStampTextView.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(item.trackTimeMillis)

        itemView.setOnClickListener {
            onTrackClicked.invoke(item)
        }

        itemView.setOnLongClickListener {
            onTrackClickedLong.invoke(item)
            true
        }
    }
}