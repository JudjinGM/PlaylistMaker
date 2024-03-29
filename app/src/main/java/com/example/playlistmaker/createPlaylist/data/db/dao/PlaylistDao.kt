package com.example.playlistmaker.createPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    fun getPlaylistByIdFlow(playlistId: Long): Flow<PlaylistWithSongs?>

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistWithSongs?

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylistWithSongsFlow(): Flow<List<PlaylistWithSongs>>

}
