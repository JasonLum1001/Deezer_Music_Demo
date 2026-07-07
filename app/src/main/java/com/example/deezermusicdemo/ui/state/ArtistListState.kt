package com.example.deezermusicdemo.ui.state

import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem

sealed interface ArtistListState {
    data object Loading: ArtistListState
    data object NetworkError: ArtistListState
    data class Success(
        val artistInfo: ArtistItem,
        val artistTracks: List<MusicItem>
    ): ArtistListState
    data class Error(
        val message: String
    ) : ArtistListState
}