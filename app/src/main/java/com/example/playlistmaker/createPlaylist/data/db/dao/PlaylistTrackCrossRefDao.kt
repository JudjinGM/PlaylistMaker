package com.example.playlistmaker.createPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistTrackCrossRefEntity

@Dao
interface PlaylistTrackCrossRefDao {
    @Insert(entity = PlaylistTrackCrossRefEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrackCrossRef(playlistTrackCrossRefEntity: PlaylistTrackCrossRefEntity)

    @Delete(entity = PlaylistTrackCrossRefEntity::class)
    suspend fun deletePlaylistTrackCrossRef(playlistTrackCrossRefEntity: PlaylistTrackCrossRefEntity)

    @Query("DELETE FROM PlaylistTrackCrossRefEntity WHERE playlistId =:playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylistTrackCrossRef(playlistId: Long, trackId: Long)

    @Query("DELETE FROM PlaylistTrackCrossRefEntity WHERE playlistId = :playlistId")
    suspend fun deletePlaylistFromPlaylistTrackCrossRef(playlistId: Long)

    @Query("SELECT EXISTS(SELECT * FROM PlaylistTrackCrossRefEntity WHERE trackId = :trackId)")
    suspend fun isTrackIdExistCrossRef(trackId: Long): Boolean
}