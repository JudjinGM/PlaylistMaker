package com.example.playlistmaker.search.ui.model

import android.os.Parcel
import android.os.Parcelable
import com.example.playlistmaker.search.domain.model.Track
import java.util.ArrayList

data class SavedTracks(var tracks: ArrayList<Track>?):Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Track)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(tracks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SavedTracks> {
        override fun createFromParcel(parcel: Parcel): SavedTracks {
            return SavedTracks(parcel)
        }

        override fun newArray(size: Int): Array<SavedTracks?> {
            return arrayOfNulls(size)
        }
    }

    fun setTracks(tracks: List<Track>){
        this.tracks = ArrayList(tracks)
    }
}