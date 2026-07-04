package com.example.deezermusicdemo.data.remote

import com.example.deezermusicdemo.data.model.DeezerArtistListResponse
import com.example.deezermusicdemo.data.model.DeezerTrackListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApiService {
    @GET("search")
    suspend fun searchTracks(@Query("q") query: String): DeezerTrackListResponse

    @GET("chart/0/artists")
    suspend fun getRecommendedArtists(): DeezerArtistListResponse

    @GET("chart/0/tracks")
    suspend fun getRecommendedTracks(): DeezerTrackListResponse

    @GET("artists/{artistsId}/tracks")
    suspend fun getTracksFromArtistId( @Path("artistsId") artistsId: Int): DeezerTrackListResponse

    companion object {
        const val BASE_URL = "https://api.deezer.com/"
    }
}
