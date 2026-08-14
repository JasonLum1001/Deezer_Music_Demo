package com.example.deezermusicdemo.common.player

import android.content.ComponentName
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.deezermusicdemo.service.MusicService
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.utils.TimeUtils
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi

@Singleton
@OptIn(UnstableApi::class)
class MusicPlayerConnection @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val TAG = "MusicPlayerConnection"
    }
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var positionJob: Job? = null

    private val _playerState = MutableStateFlow(MusicPlayerState())

    val playerState = _playerState.asStateFlow()


    init {
        connect()
    }

    private fun connect() {
        Log.d(TAG, "connect")
        val token = SessionToken(
            context,
            ComponentName(context, MusicService::class.java)
        )

        controllerFuture = MediaController.Builder(context, token).buildAsync()

        controllerFuture?.addListener(
            {
                controller = controllerFuture?.get()
                setupListener()
                updateState()
            },
            MoreExecutors.directExecutor()
        )
    }


    private fun setupListener() {
        Log.d(TAG, "setupListener")
        controller?.addListener(
            object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    updateState()
                    if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
                        if (player.isPlaying) {
                            startPositionUpdate()
                        } else {
                            stopPositionUpdate()
                        }
                    }
                }


                override fun onPlayerError(error: PlaybackException) {
                    Log.d(TAG, "onPlayerError: $error")
                    _playerState.update {
                        it.copy(error = error.message)
                    }
                }
            }
        )
    }


    private fun updateState() {
        Log.d(TAG, "updateState")
        val player = controller ?: return
        val current = player.currentMediaItem
        val metadata = current?.mediaMetadata

        val song = if (metadata != null) {
            MusicItem(
                id = current.mediaId.toLong(),
                title = metadata.title?.toString() ?: "",
                artist = metadata.artist?.toString() ?: "",
                album = metadata.albumTitle?.toString() ?: "",
                albumArt = metadata.artworkUri?.toString() ?: "",
                previewUrl = current.localConfiguration?.uri?.toString() ?: "",
                duration = TimeUtils.millisecondToSecond(player.duration)
            )
        } else null

        _playerState.update {
            it.copy(
                currentSong = song,
                isPlaying = player.isPlaying,
                isBuffering = player.playbackState == Player.STATE_BUFFERING,
                currentPosition = player.currentPosition,
                duration = if (player.duration > 0) player.duration else 0L,
                repeatMode = player.repeatMode,
                shuffleEnabled = player.shuffleModeEnabled
            )
        }
    }


    fun setPlaylist(musicItems: List<MusicItem>, startIndex: Int = 0) {
        Log.d(TAG, "setPlaylist => musicItems: ${musicItems.size}, startIndex: $startIndex")
        val mediaItems = musicItems.map { createMediaItem(it) }

        controller?.let { player ->
            player.setMediaItems(
                mediaItems,
                startIndex,
                0L
            )

            player.prepare()
            player.play()
        }

        _playerState.update {
            it.copy(
                playlist = musicItems
            )
        }

    }

    fun togglePlayPause() {
        Log.d(TAG, "togglePlayPause")
        controller?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }


    fun seekTo(position: Long) {
        Log.d(TAG, "seekTo => position: $position")
        controller?.seekTo(position)
    }

    fun next() {
        Log.d(TAG, "seekToNext")
        controller?.seekToNext()
    }

    fun previous() {
        Log.d(TAG, "seekToPrevious")
        controller?.seekToPrevious()
    }

    fun toggleRepeat() {
        Log.d(TAG, "toggleRepeat")
        controller?.let {
            it.repeatMode =
                when (it.repeatMode) {
                    Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                    Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                    else -> Player.REPEAT_MODE_OFF
                }
        }
    }

    fun toggleShuffle() {
        Log.d(TAG, "toggleShuffle")
        controller?.let {
            it.shuffleModeEnabled = !it.shuffleModeEnabled
        }
    }

    fun addToQueue(
        song: MusicItem
    ) {
        controller?.addMediaItem(
            createMediaItem(song)
        )

        _playerState.update {
            it.copy(
                playlist = it.playlist + song
            )
        }
    }

    fun removeFromQueue(
        index: Int
    ) {
        controller?.removeMediaItem(index)
        _playerState.update {
            it.copy(
                playlist = it.playlist.toMutableList().apply {
                    removeAt(index)
                }
            )
        }
    }

    private fun createMediaItem(
        song: MusicItem
    ): MediaItem {
        Log.d(TAG, "createMediaItem =>" +
                "id: ${song.id},\n" +
                "title: ${song.title}\n" +
                "artist: ${song.artist}\n" +
                "album: ${song.album}\n" +
                "albumArt: ${song.albumArt}\n" +
                "previewUrl: ${song.previewUrl}\n" +
                "duration: ${song.duration}\n" +
                "isBookmarked: ${song.isBookmarked}"
        )
        return MediaItem.Builder()
            .setMediaId(song.id.toString())
            .setUri(song.previewUrl)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(song.title)
                    .setArtist(song.artist)
                    .setAlbumTitle(song.album)
                    .setArtworkUri(song.albumArt.toUri())
                    .setIsPlayable(true)
                    .build()
            )
            .build()
    }


    private fun startPositionUpdate() {
        Log.d(TAG, "startPositionUpdate")
        positionJob?.cancel()
        positionJob = scope.launch {
            while (isActive) {
                controller?.let {
                    _playerState.update { state ->
                        state.copy(
                            currentPosition = it.currentPosition,
                            duration = it.duration
                        )
                    }
                }
                delay(200)
            }
        }
    }


    private fun stopPositionUpdate() {
        Log.d(TAG, "stopPositionUpdate")
        positionJob?.cancel()
    }


    fun release() {
        Log.d(TAG, "release")
        positionJob?.cancel()
        scope.cancel()
        controller?.release()
        controller = null
        controllerFuture?.cancel(true)
    }
}
