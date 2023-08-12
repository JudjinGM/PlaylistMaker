package com.example.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTracksDao {
    @Insert(entity = FavoriteTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(trackEntity: FavoriteTrackEntity)

    @Delete(entity = FavoriteTrackEntity::class)
    suspend fun deleteFavoriteTrack(trackEntity: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks_table ORDER BY timeStamp DESC")
    fun getFavoriteTracksFlow(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT trackId FROM favorite_tracks_table")
    fun getFavoritesTracksIdFlow():Flow<List<Long>>

    @Query("SELECT trackId FROM favorite_tracks_table")
    suspend fun getFavoritesTracksId():List<Long>
}