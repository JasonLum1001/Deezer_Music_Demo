package com.example.deezermusicdemo.data.remote

import com.example.deezermusicdemo.data.model.DeezerArtist
import com.example.deezermusicdemo.data.model.DeezerArtistListResponse
import com.example.deezermusicdemo.data.model.DeezerTrackListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApiService {
    @GET("search")
    suspend fun searchTracks(@Query("q") query: String): DeezerTrackListResponse

    @GET("chart/0/artists")
    suspend fun getRecommendedArtists(@Query("limit") limit: Int = 10): DeezerArtistListResponse

    @GET("chart/0/tracks")
    suspend fun getRecommendedTracks(@Query("limit") limit: Int = 50): DeezerTrackListResponse

    @GET("artist/{artistId}")
    suspend fun getArtistFromId( @Path("artistId") artistId: Long): DeezerArtist

    @GET("artist/{artistId}/top")
    suspend fun getTracksFromArtistId( @Path("artistId") artistId: Long, @Query("limit") limit: Int = 50): DeezerTrackListResponse

    companion object {
        const val BASE_URL = "https://api.deezer.com/"
    }
}
