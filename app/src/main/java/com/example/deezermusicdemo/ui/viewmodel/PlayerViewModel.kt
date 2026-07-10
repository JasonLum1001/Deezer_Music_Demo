package com.example.deezermusicdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.deezermusicdemo.common.player.MusicPlayerConnection
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: MusicPlayerConnection
) : ViewModel() {
    val state = player.playerState

    fun playPlaylist(musicItems: List<MusicItem>, startIndex: Int = 0) {
        player.setPlaylist(musicItems, startIndex)
    }

    fun playPause() {
        player.togglePlayPause()
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun skipNext() {
        player.next()
    }

    fun skipPrevious() {
        player.previous()
    }

    fun toggleRepeatMode() {
        player.toggleRepeat()
    }

    fun toggleShuffleMode() {
        player.toggleShuffle()
    }

}
