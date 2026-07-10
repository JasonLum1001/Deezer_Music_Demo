package com.example.deezermusicdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.ui.state.BookmarkListState
import com.example.deezermusicdemo.ui.state.SearchListState
import com.example.deezermusicdemo.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val bookmarkList = repository.bookmarkList

    val uiState = combine(
        _loading,
        _error,
        bookmarkList
    ) { loading, error, bookmarkList ->

        when {
            loading -> BookmarkListState.Loading
            error != null -> BookmarkListState.Error(error)
            bookmarkList.isEmpty()  -> BookmarkListState.Empty
            else -> BookmarkListState.Success(
                bookmarkList = bookmarkList
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchListState.Loading
    )

    fun toggleBookmark(item: MusicItem) {
        viewModelScope.launch {
            repository.toggleBookmark(item)
        }
    }
}
