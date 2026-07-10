package com.example.deezermusicdemo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumArt: String,
    val previewUrl: String,
    val duration: Long,
    val isBookmarked: Boolean,
)
