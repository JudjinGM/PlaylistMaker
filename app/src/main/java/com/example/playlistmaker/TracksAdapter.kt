package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(private val trackList: List<Track>) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent)
    }

    override fun getItemCount() = trackList.size


    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
}