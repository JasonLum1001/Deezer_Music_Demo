package com.example.deezermusicdemo.ui.state

import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem

sealed interface BookmarkListState {
    data object Loading: BookmarkListState
    data object Empty: BookmarkListState
    data class Success(
        val bookmarkList: List<MusicItem>
    ): BookmarkListState
    data class Error(
        val message: String
    ) : BookmarkListState
}