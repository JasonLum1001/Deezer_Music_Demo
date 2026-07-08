package com.example.deezermusicdemo.data.mapper

import com.example.deezermusicdemo.data.model.DeezerArtist
import com.example.deezermusicdemo.data.model.DeezerTrack
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.utils.TimeUtils

fun DeezerTrack.toMusicItem(): MusicItem {
    return MusicItem(
        id = id,
        title = title,
        artist = artist.name,
        album = album.title,
        albumArt = album.cover,
        previewUrl = preview,
        duration = TimeUtils.secondToDuration(duration)
    )
}

fun DeezerArtist.toArtistItem(): ArtistItem {
    return ArtistItem(
        id = id,
        name = name,
        thumbnail = profile
    )
}
