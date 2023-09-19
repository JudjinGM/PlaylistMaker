package com.example.playlistmaker.search.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Track(
    val trackId: Long = 0L,
    val trackName: String = "Unknown song",
    val artistName: String = "",
    val trackTimeMillis: Long = 0,
    val artworkUrl100: String = "",
    val artworkUrl60: String = "",
    val collectionName: String = "",
    val releaseDate: String = "",
    val country: String = "",
    val primaryGenreName: String = "",
    val previewUrl: String = "",
    var isFavorite: Boolean = false

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readBoolean()
    )

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getShortReleaseDate() = releaseDate.take(4)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeLong(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeString(artworkUrl60)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(country)
        parcel.writeString(primaryGenreName)
        parcel.writeString(previewUrl)
        parcel.writeBoolean(isFavorite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}