package com.example.deezermusicdemo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.ui.state.ArtistListState
import com.example.deezermusicdemo.ui.state.HomeListState
import com.example.deezermusicdemo.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val repository: MusicRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor
) : ViewModel() {
    val artistId: Long = checkNotNull(savedStateHandle["artistId"])

    private val _artistInfo = repository.artistInfo

    private val _loading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    private val networkConnected = networkMonitor.isConnected

    val uiState = combine(
        _loading,
        _error,
        networkConnected,
        _artistInfo
    ) { loading, error, connected, artistInfo ->

        when {
            (!connected && artistInfo == null) -> ArtistListState.NetworkError
            loading -> ArtistListState.Loading
            artistInfo == null || error != null -> ArtistListState.Error(error ?: "")
            else -> ArtistListState.Success(
                artistInfo = artistInfo.info,
                artistTracks = artistInfo.tracks
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArtistListState.Loading
    )

    init {
        loadArtistFromId(artistId)
    }

    fun retry() {
        loadArtistFromId(artistId)
    }

    private fun loadArtistFromId(artistId: Long) {
        viewModelScope.launch {
            repository.clearArtistInfo()
            _loading.value = true
            _error.value = null

            runCatching {
                coroutineScope {
                    repository.getArtistInfoFromId(artistId)
                }
            }.onFailure {
                _error.value = it.message
            }

            _loading.value = false
        }
    }

    fun toggleBookmark(item: MusicItem) {
        viewModelScope.launch {
            repository.toggleBookmark(item)
        }
    }
}
