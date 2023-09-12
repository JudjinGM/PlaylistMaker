package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.createPlaylist.data.db.dao.PlaylistDao
import com.example.playlistmaker.createPlaylist.data.db.dao.PlaylistTrackCrossRefDao
import com.example.playlistmaker.createPlaylist.data.db.dao.TrackDao
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.TrackEntity

@Database(
    entities = [FavoriteTrackEntity::class, TrackEntity::class, PlaylistEntity::class, PlaylistTrackCrossRefEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackDao(): TrackDao
    abstract fun playlistTrackCrossRefDao(): PlaylistTrackCrossRefDao
}