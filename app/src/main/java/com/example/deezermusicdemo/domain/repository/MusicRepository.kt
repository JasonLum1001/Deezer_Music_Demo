package com.example.deezermusicdemo.domain.repository

import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem

interface MusicRepository {
    suspend fun searchTracks(query: String): List<MusicItem>
    suspend fun getRecommendedTracks(): List<MusicItem>
    suspend fun getRecommendedArtists(): List<ArtistItem>
    suspend fun getArtistFromId(artistId: Long): ArtistItem
    suspend fun getTracksFromArtistId(artistId: Long): List<MusicItem>
}