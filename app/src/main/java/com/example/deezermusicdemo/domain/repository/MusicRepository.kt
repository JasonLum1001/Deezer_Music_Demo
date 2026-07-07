package com.example.deezermusicdemo.domain.repository

import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import kotlinx.coroutines.flow.StateFlow

interface MusicRepository {
    val recommendedMusic: StateFlow<List<MusicItem>>
    val recommendedArtist: StateFlow<List<ArtistItem>>

    suspend fun searchTracks(query: String): List<MusicItem>
    suspend fun refreshRecommendations()
    suspend fun getArtistFromId(artistId: Long): ArtistItem
    suspend fun getTracksFromArtistId(artistId: Long): List<MusicItem>
}