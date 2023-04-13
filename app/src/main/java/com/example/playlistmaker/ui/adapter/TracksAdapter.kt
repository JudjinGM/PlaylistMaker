package com.example.playlistmaker.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.model.Track

class TracksAdapter(
    var tracks: List<Track>,
    var onTrackClicked: ((Track) -> Unit)={ }
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent, onTrackClicked)
    }

    override fun getItemCount() = tracks.size


    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    fun updateAdapter(newTracks: List<Track>){
        val diffResult = getDiffResult(tracks, newTracks)
        tracks = newTracks
        diffResult.dispatchUpdatesTo(this)
    }
    private fun getDiffResult(oldList: List<Track>, newList: List<Track>): DiffUtil.DiffResult {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })
        return diffResult
    }

}