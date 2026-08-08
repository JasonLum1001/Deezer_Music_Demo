package com.example.deezermusicdemo.data.mapper

import com.example.deezermusicdemo.data.model.DeezerArtist
import com.example.deezermusicdemo.data.model.DeezerTrack
import com.example.deezermusicdemo.data.model.TrackEntity
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem

fun DeezerTrack.toMusicItem(isBookmarked: Boolean): MusicItem {
    return MusicItem(
        id = id,
        title = title,
        artist = artist.name,
        album = album.title,
        albumArt = album.cover,
        previewUrl = preview,
        duration = duration,
        isBookmarked = isBookmarked
    )
}

fun DeezerArtist.toArtistItem(): ArtistItem {
    return ArtistItem(
        id = id,
        name = name,
        thumbnail = profile
    )
}

fun MusicItem.toEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        title = title,
        artist = artist,
        album = album,
        albumArt = albumArt,
        previewUrl = previewUrl,
        duration = duration,
        isBookmarked = isBookmarked
    )
}

fun TrackEntity.toMusicItem(): MusicItem {
    return MusicItem(
        id = id,
        title = title,
        artist = artist,
        album = album,
        albumArt = albumArt,
        previewUrl = previewUrl,
        duration = duration,
        isBookmarked = isBookmarked
    )
}
