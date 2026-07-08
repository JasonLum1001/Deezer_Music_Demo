package com.example.deezermusicdemo.domain.model

data class ArtistInfo(
    val info: ArtistItem,
    val tracks: List<MusicItem>
)

data class ArtistItem(
    val id: Long,
    val name: String,
    val thumbnail: String,
)
