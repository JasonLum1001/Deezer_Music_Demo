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

    private val _artistInfo = MutableStateFlow<ArtistItem?>(null)

    private val _artistTracks = MutableStateFlow<List<MusicItem>>(emptyList())

    private val _loading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    private val networkConnected = networkMonitor.isConnected

    val uiState = combine(
        _loading,
        _error,
        networkConnected,
        _artistInfo,
        _artistTracks
    ) { loading, error, connected, artistInfo, artistTracks ->

        when {
            (!connected && artistInfo == null && artistTracks.isEmpty()) -> ArtistListState.NetworkError
            loading -> ArtistListState.Loading
            artistInfo == null || artistTracks.isEmpty() || error != null -> ArtistListState.Error(error ?: "")
            else -> ArtistListState.Success(
                artistInfo = artistInfo,
                artistTracks = artistTracks
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
            _loading.value = true
            _error.value = null

            runCatching {
                coroutineScope {
                    val info = async {
                        repository.getArtistFromId(artistId)
                    }
                    val tracks = async {
                        repository.getTracksFromArtistId(artistId)
                    }

                    _artistInfo.value = info.await()
                    _artistTracks.value = tracks.await()
                }
            }.onFailure {
                _error.value = it.message
            }

            _loading.value = false
        }
    }
}
