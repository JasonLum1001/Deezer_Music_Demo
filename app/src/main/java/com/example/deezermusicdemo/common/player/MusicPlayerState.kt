package com.example.deezermusicdemo.common.player

import androidx.media3.common.Player
import com.example.deezermusicdemo.domain.model.MusicItem

data class MusicPlayerState(
    val currentSong: MusicItem? = null,
    val playlist: List<MusicItem> = emptyList(),
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val repeatMode: Int = Player.REPEAT_MODE_OFF,
    val shuffleEnabled: Boolean = false,
    val error: String? = null
)
