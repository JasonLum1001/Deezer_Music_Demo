package com.example.deezermusicdemo.data.repository

import android.util.Log
import com.example.deezermusicdemo.data.local.TrackDao
import com.example.deezermusicdemo.data.mapper.toArtistItem
import com.example.deezermusicdemo.data.mapper.toEntity
import com.example.deezermusicdemo.data.remote.DeezerApiService
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.data.mapper.toMusicItem
import com.example.deezermusicdemo.domain.model.ArtistInfo
import com.example.deezermusicdemo.domain.model.ArtistItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val apiService: DeezerApiService,
    private val trackDao: TrackDao,
) : MusicRepository {

    private val _recommendedMusic = MutableStateFlow<List<MusicItem>>(emptyList())
    override val recommendedMusic = _recommendedMusic.asStateFlow()

    private val _recommendedArtist = MutableStateFlow<List<ArtistItem>>(emptyList())
    override val recommendedArtist = _recommendedArtist.asStateFlow()

    private val _searchResult = MutableStateFlow<List<MusicItem>>(emptyList())
    override val searchResult = _searchResult.asStateFlow()

    private val _artistInfo = MutableStateFlow<ArtistInfo?>(null)
    override val artistInfo = _artistInfo.asStateFlow()

    override val bookmarkList = trackDao.getBookmarkedTracks().map { entities ->
        entities.map { it.toMusicItem() }
    }

    override suspend fun refreshRecommendations() {
        Log.d("MusicRepository", "refreshRecommendations")
        coroutineScope {
            val music = async {
                apiService.getRecommendedTracks()
                    .data
                    .map { it.toMusicItem() }
            }

            val artists = async {
                apiService.getRecommendedArtists()
                    .data
                    .map { it.toArtistItem() }
            }

            _recommendedMusic.value = music.await()
            _recommendedArtist.value = artists.await()
        }
    }

    override suspend fun searchTracks(query: String) {
        Log.d("MusicRepository", "searchTracks: $query")

        val result = apiService.searchTracks(query).data.map { it.toMusicItem() }
        _searchResult.value = result
    }

    override suspend fun clearSearchResult() {
        Log.d("MusicRepository", "clearSearchResult")

        _searchResult.value = emptyList()
    }

    override suspend fun getArtistInfoFromId(artistId: Long) {
        Log.d("MusicRepository", "getTracksFromArtistId")

        coroutineScope {
            val info = async {
                apiService.getArtistFromId(artistId).toArtistItem()
            }

            val tracks = async {
                apiService.getTracksFromArtistId(artistId).data.map { it.toMusicItem() }
            }

            _artistInfo.value = ArtistInfo(
                info = info.await(),
                tracks = tracks.await()
            )

        }
    }

    override suspend fun clearArtistInfo() {
        Log.d("MusicRepository", "clearArtistInfo")

        _artistInfo.value = null
    }

    override suspend fun toggleBookmark(item: MusicItem) {
        Log.d("MusicRepository", "toggleBookmark=> ${item.id}")

        val existing = trackDao.getTrackById(item.id)
        val isBookmarked = if (existing == null) {
            trackDao.insertTrack(item.copy(isBookmarked = true).toEntity())
            true
        } else {
            val newState = !existing.isBookmarked
            trackDao.updateBookmark(item.id, newState)
            newState
        }
        updateBookmarkState(item.id, isBookmarked)
    }

    private fun updateBookmarkState(
        musicId: Long,
        isBookmarked: Boolean
    ) {
        _recommendedMusic.update { list ->
            list.map { music ->
                if (music.id == musicId) {
                    music.copy(isBookmarked = isBookmarked)
                } else {
                    music
                }
            }
        }

        _searchResult.update { list ->
            list.map { music ->
                if (music.id == musicId) {
                    music.copy(isBookmarked = isBookmarked)
                } else {
                    music
                }
            }
        }
    }
}