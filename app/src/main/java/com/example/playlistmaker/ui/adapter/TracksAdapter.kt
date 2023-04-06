package com.example.playlistmaker.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.model.Track

class TracksAdapter(private val track: MutableList<Track>) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent)
    }

    override fun getItemCount() = track.size


    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(track[position])
    }
}