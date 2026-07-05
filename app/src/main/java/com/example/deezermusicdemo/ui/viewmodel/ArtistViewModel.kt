package com.example.deezermusicdemo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MusicRepository
) : ViewModel() {
    val artistId: Long = checkNotNull(savedStateHandle["artistId"])

    private val _artistInfo = MutableStateFlow<ArtistItem?>(null)
    val artistInfo = _artistInfo.asStateFlow()

    private val _artistTracks = MutableStateFlow<List<MusicItem>>(emptyList())
    val artistTracks = _artistTracks.asStateFlow()

    init {
        loadArtistFromId(artistId)
        loadTrackFromArtist(artistId)
    }

    private fun loadArtistFromId(artistId: Long) {
        viewModelScope.launch {
            Log.d("ArtistViewModel", "loadArtistFromId: $artistId")
            try {
                _artistInfo.value = repository.getArtistFromId(artistId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadTrackFromArtist(artistId: Long) {
        viewModelScope.launch {
            Log.d("ArtistViewModel", "loadTrackFromArtist: $artistId")
            try {
                _artistTracks.value = repository.getTracksFromArtistId(artistId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
