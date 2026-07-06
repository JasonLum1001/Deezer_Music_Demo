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
class SearchViewModel @Inject constructor(
    private val repository: MusicRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<MusicItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    init {
        loadRecommendations()
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            try {
                _searchResults.value = repository.searchTracks(query)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            try {
                _searchResults.value = repository.getRecommendedTracks()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
