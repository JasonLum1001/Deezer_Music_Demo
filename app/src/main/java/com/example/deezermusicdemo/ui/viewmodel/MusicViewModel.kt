package com.example.deezermusicdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.ui.state.HomeListState
import com.example.deezermusicdemo.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val repository: MusicRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _recommendedMusic = repository.recommendedMusic
    private val _recommendedArtist = repository.recommendedArtist
    private val _loading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    private val networkConnected = networkMonitor.isConnected

    val uiState = combine(
        _loading,
        _error,
        networkConnected,
        _recommendedMusic,
        _recommendedArtist
    ) { loading, error, connected, music, artists ->

        when {
            (!connected && music.isEmpty() && artists.isEmpty()) -> HomeListState.NetworkError
            loading -> HomeListState.Loading
            music.isEmpty() || artists.isEmpty() || error != null -> HomeListState.Error(error ?: "")
            else -> HomeListState.Success(
                recommendedMusic = music,
                recommendedArtist = artists
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeListState.Loading
    )

    init {
        loadRecommendations()
    }

    fun retry() {
        loadRecommendations()
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            runCatching {
                repository.refreshRecommendations()
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
