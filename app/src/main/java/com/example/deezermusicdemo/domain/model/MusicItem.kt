package com.example.deezermusicdemo.domain.model

data class MusicItem(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumArt: String,
    val previewUrl: String,
    val duration: String
)
