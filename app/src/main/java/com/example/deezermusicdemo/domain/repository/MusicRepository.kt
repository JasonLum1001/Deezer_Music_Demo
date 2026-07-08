package com.example.deezermusicdemo.domain.repository

import com.example.deezermusicdemo.domain.model.ArtistInfo
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import kotlinx.coroutines.flow.StateFlow

interface MusicRepository {
    val recommendedMusic: StateFlow<List<MusicItem>>
    val recommendedArtist: StateFlow<List<ArtistItem>>
    val searchResult: StateFlow<List<MusicItem>>
    val artistInfo: StateFlow<ArtistInfo?>

    suspend fun refreshRecommendations()
    suspend fun searchTracks(query: String)
    suspend fun clearSearchResult()
    suspend fun getArtistInfoFromId(artistId: Long)
    suspend fun clearArtistInfo()}