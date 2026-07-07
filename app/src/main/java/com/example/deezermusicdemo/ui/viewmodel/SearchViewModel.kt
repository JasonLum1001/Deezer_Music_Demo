package com.example.deezermusicdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.ui.state.SearchListState
import com.example.deezermusicdemo.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MusicRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    private val _searchResults = MutableStateFlow(repository.recommendedMusic.value)

    private val _loading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val networkConnected = networkMonitor.isConnected

    val uiState = combine(
        _loading,
        _error,
        networkConnected,
        _searchResults,
    ) { loading, error, connected, searchResult ->

        when {
            (!connected && searchResult.isEmpty()) -> SearchListState.NetworkError
            loading -> SearchListState.Loading
            searchResult.isEmpty() || error != null -> SearchListState.Error(error ?: "")
            else -> SearchListState.Success(
                searchResult = searchResult,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchListState.Loading
    )

    init {
        if (repository.recommendedMusic.value.isEmpty() && _searchQuery.value.isNullOrEmpty()) {
            loadRecommendations()
        }
    }

    fun retry() {
        val query = _searchQuery.value
        if (query != null) {
            searchTracks(query)
        } else {
            loadRecommendations()
        }
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

    fun searchTracks(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query
            _loading.value = true
            _error.value = null

            runCatching {
                _searchResults.value = repository.searchTracks(query)
            }.onFailure {
                _error.value = it.message
            }

            _loading.value = false
        }
    }
}
