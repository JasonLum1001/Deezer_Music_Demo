package com.example.deezermusicdemo.data.model

import com.google.gson.annotations.SerializedName

data class DeezerTrackListResponse(
    @SerializedName("data") val data: List<DeezerTrack>
)

data class DeezerArtistListResponse(
    @SerializedName("data") val data: List<DeezerArtist>
)

data class DeezerTrack(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("preview") val preview: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("artist") val artist: DeezerArtist,
    @SerializedName("album") val album: DeezerAlbum
)

data class DeezerArtist(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("picture_medium") val profile: String
)

data class DeezerAlbum(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("cover_medium") val cover: String
)
