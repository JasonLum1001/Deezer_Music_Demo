package com.example.deezermusicdemo.data.repository

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
        return apiService.searchTracks(query).data.map { it.toMusicItem() }
    }

    override suspend fun getRecommendedTracks(): List<MusicItem> {
        return apiService.getRecommendedTracks().data.map { it.toMusicItem() }
    }

    override suspend fun getRecommendedArtists(): List<ArtistItem> {
        return apiService.getRecommendedArtists().data.map { it.toArtistItem() }
    }
}