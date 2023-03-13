package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val albumCover: ImageView = itemView.findViewById(R.id.albumCoverImageView)
    private val trackName: TextView = itemView.findViewById(R.id.songNameTextView)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val songTime: TextView = itemView.findViewById(R.id.songTimeStampTextView)

    fun bind(item:Track){
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.no_album)
            .centerInside()
            .transform(RoundedCorners(10))
            .into(albumCover)
        trackName.text = item.trackName
        artistName.text = item.artistName
        songTime.text = item.trackTime
    }
}