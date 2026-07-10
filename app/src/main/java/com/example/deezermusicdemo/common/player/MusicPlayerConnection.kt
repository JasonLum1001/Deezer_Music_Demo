package com.example.deezermusicdemo.common.player

import android.content.ComponentName
import android.content.Context
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

@Singleton
class MusicPlayerConnection @Inject constructor(
    private val context: Context
) {
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
                    _playerState.update {
                        it.copy(error = error.message)
                    }
                }
            }
        )
    }


    private fun updateState() {
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
        controller?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }


    fun seekTo(position: Long) {
        controller?.seekTo(position)
    }

    fun next() {
        controller?.seekToNext()
    }

    fun previous() {
        controller?.seekToPrevious()
    }

    fun toggleRepeat() {
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
        positionJob?.cancel()
    }


    fun release() {
        positionJob?.cancel()
        scope.cancel()
        controller?.release()
        controller = null
        controllerFuture?.cancel(true)
    }
}
