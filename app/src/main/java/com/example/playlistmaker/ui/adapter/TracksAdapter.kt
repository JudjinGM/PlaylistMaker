package com.example.playlistmaker.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.model.Track

class TracksAdapter(private val tracks: MutableList<Track>, var onTrackClicked: ((Track) -> Unit)? = null) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent, onTrackClicked)
    }

    override fun getItemCount() = tracks.size


    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }
}