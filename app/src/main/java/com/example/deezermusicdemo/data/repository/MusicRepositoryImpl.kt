package com.example.deezermusicdemo.data.repository

import android.util.Log
import com.example.deezermusicdemo.data.mapper.toArtistItem
import com.example.deezermusicdemo.data.remote.DeezerApiService
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.data.mapper.toMusicItem
import com.example.deezermusicdemo.domain.model.ArtistInfo
import com.example.deezermusicdemo.domain.model.ArtistItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val apiService: DeezerApiService
) : MusicRepository {

    private val _recommendedMusic = MutableStateFlow<List<MusicItem>>(emptyList())
    override val recommendedMusic = _recommendedMusic.asStateFlow()

    private val _recommendedArtist = MutableStateFlow<List<ArtistItem>>(emptyList())
    override val recommendedArtist = _recommendedArtist.asStateFlow()

    private val _searchResult = MutableStateFlow<List<MusicItem>>(emptyList())
    override val searchResult = _searchResult.asStateFlow()

    private val _artistInfo = MutableStateFlow<ArtistInfo?>(null)
    override val artistInfo = _artistInfo.asStateFlow()

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
}