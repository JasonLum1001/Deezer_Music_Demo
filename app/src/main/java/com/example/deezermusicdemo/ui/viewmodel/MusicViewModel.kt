package com.example.deezermusicdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val repository: MusicRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val isNetworkConnected = networkMonitor.isConnected

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MusicItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _recommendedMusic = MutableStateFlow<List<MusicItem>>(emptyList())
    val recommendedMusic = _recommendedMusic.asStateFlow()

    private val _recommendedArtistItem = MutableStateFlow<List<ArtistItem>>(emptyList())
    val recommendedArtist = _recommendedArtistItem.asStateFlow()

    init {
        loadRecommendations()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length >= 2) {
            searchTracks(query)
        } else {
            _searchResults.value = emptyList()
        }
    }

    private fun searchTracks(query: String) {
        viewModelScope.launch {
            try {
                _searchResults.value = repository.searchTracks(query)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadRecommendations() {
        if (!isNetworkConnected.value) return
        viewModelScope.launch {
            try {
                _recommendedMusic.value = repository.getRecommendedTracks()
                _recommendedArtistItem.value = repository.getRecommendedArtists()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
