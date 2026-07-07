package com.example.deezermusicdemo.ui.state

import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem

sealed interface HomeListState {
    data object Loading: HomeListState
    data object NetworkError: HomeListState
    data class Success(
        val recommendedMusic: List<MusicItem>,
        val recommendedArtist: List<ArtistItem>
    ): HomeListState
    data class Error(
        val message: String
    ) : HomeListState
}