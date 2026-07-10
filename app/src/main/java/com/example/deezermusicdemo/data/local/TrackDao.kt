package com.example.deezermusicdemo.data.local

import androidx.room.*
import com.example.deezermusicdemo.data.model.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks WHERE isBookmarked = 1")
    fun getBookmarkedTracks(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrackById(id: Long): TrackEntity?
    
    @Query("UPDATE tracks SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Long, isBookmarked: Boolean)
}
