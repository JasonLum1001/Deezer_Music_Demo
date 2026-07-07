package com.example.deezermusicdemo.data.repository

import android.util.Log
import com.example.deezermusicdemo.data.mapper.toArtistItem
import com.example.deezermusicdemo.data.remote.DeezerApiService
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.data.mapper.toMusicItem
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

    override suspend fun searchTracks(query: String): List<MusicItem> {
        val result = apiService.searchTracks(query).data.map { it.toMusicItem() }
        Log.d("MusicRepository", "searchTracks: $query -> ${result.count()}")
        return result
    }

    override suspend fun refreshRecommendations() {
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

    override suspend fun getArtistFromId(artistId: Long): ArtistItem {
        val result = apiService.getArtistFromId(artistId).toArtistItem()
        Log.d("MusicRepository", "getArtistFromId: $artistId -> $result")
        return result
    }

    override suspend fun getTracksFromArtistId(artistId: Long): List<MusicItem> {
        val result = apiService.getTracksFromArtistId(artistId).data.map { it.toMusicItem() }
        Log.d("MusicRepository", "getTracksFromArtistId: $artistId -> ${result.count()}")
        return result
    }
}