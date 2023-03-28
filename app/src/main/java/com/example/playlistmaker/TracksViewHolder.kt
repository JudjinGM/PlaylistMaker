package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TracksViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context).inflate(R.layout.tracks_view, parentView, false)

) {
    private val albumCover: ImageView = itemView.findViewById(R.id.albumCoverImageView)
    private val trackName: TextView = itemView.findViewById(R.id.songNameTextView)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val songTime: TextView = itemView.findViewById(R.id.songTimeStampTextView)

    fun bind(item: Track) {
        Glide.with(itemView).load(item.artworkUrl100).placeholder(R.drawable.no_album)
            .centerInside()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)))
            .into(albumCover)
        trackName.text = item.trackName
        artistName.text = item.artistName
        songTime.text = item.trackTime
    }
}