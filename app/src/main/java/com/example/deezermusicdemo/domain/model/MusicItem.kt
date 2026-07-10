package com.example.deezermusicdemo.domain.model

import com.example.deezermusicdemo.utils.TimeUtils

data class MusicItem(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumArt: String,
    val previewUrl: String,
    val duration: Long,
    val isBookmarked: Boolean = false
) {
    val durationStr = TimeUtils.secondToDuration(duration)
}
