package com.example.deezermusicdemo.ui.state

import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem

sealed interface SearchListState {
    data object Loading: SearchListState
    data object NetworkError: SearchListState
    data class Success(
        val searchResult: List<MusicItem>
    ): SearchListState
    data class Error(
        val message: String
    ) : SearchListState
}