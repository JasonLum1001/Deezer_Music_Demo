package com.example.deezermusicdemo.data.repository

import android.util.Log
import com.example.deezermusicdemo.data.mapper.toArtistItem
import com.example.deezermusicdemo.data.remote.DeezerApiService
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.domain.repository.MusicRepository
import com.example.deezermusicdemo.data.mapper.toMusicItem
import com.example.deezermusicdemo.domain.model.ArtistItem
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val apiService: DeezerApiService
) : MusicRepository {
    override suspend fun searchTracks(query: String): List<MusicItem> {
        val result = apiService.searchTracks(query).data.map { it.toMusicItem() }
        Log.d("MusicRepository", "searchTracks: $query -> ${result.count()}")
        return result
    }

    override suspend fun getRecommendedTracks(): List<MusicItem> {
        val result = apiService.getRecommendedTracks().data.map { it.toMusicItem() }
        Log.d("MusicRepository", "getRecommendedTracks -> ${result.count()}")
        return result
    }

    override suspend fun getRecommendedArtists(): List<ArtistItem> {
        val result = apiService.getRecommendedArtists().data.map { it.toArtistItem() }
        Log.d("MusicRepository", "getRecommendedArtists -> ${result.count()}")
        return result
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