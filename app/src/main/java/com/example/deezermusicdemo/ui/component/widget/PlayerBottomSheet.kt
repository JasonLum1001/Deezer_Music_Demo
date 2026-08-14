package com.example.deezermusicdemo.ui.component.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.player.MusicPlayerState
import com.example.deezermusicdemo.utils.TimeUtils
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerBottomSheet(
    modifier: Modifier = Modifier,
    playerStateFlow: StateFlow<MusicPlayerState>,
    onSeek: (Float) -> Unit,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleShuffle: () -> Unit,
    onDismiss: () -> Unit,
) {
    val playerState by playerStateFlow.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        PlayerContent(
            playerStateLambda = { playerState },
            onSeek = onSeek,
            onPlayPause = onPlayPause,
            onSkipNext = onSkipNext,
            onSkipPrevious = onSkipPrevious,
            onToggleRepeat = onToggleRepeat,
            onToggleShuffle = onToggleShuffle
        )
    }
}

@Composable
private fun PlayerContent(
    playerStateLambda: () -> MusicPlayerState,
    onSeek: (Float) -> Unit,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleShuffle: () -> Unit
) {
    val playerState = playerStateLambda()

    val musicItem = playerState.currentSong ?: return
    val duration = playerState.duration
    val isPlaying = playerState.isPlaying
    val currentPosition = playerState.currentPosition
    val repeatMode = playerState.repeatMode
    val shuffleEnabled = playerState.shuffleEnabled

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = musicItem.albumArt,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.large),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = musicItem.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = colorResource(R.color.white_100),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = musicItem.artist,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Thin,
            color = colorResource(R.color.white_80),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(52.dp))

        Slider(
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors().copy(
                thumbColor = colorResource(R.color.green_80),
                activeTrackColor = colorResource(R.color.green_60)
            ),
            value = currentPosition.toFloat(),
            onValueChange = onSeek,
            valueRange = 0f..duration.toFloat(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(TimeUtils.millisecondToDuration(currentPosition))
            Text(musicItem.durationStr)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggleRepeat) {
                Icon(
                    imageVector = when (repeatMode) {
                        1 -> Icons.Default.RepeatOne // Player.REPEAT_MODE_ONE
                        2 -> Icons.Default.Repeat // Player.REPEAT_MODE_ALL
                        else -> Icons.Default.Repeat
                    },
                    contentDescription = "Repeat Mode",
                    tint = if (repeatMode != 0) colorResource(R.color.green_80) else colorResource(R.color.white_100)
                )
            }

            IconButton(onClick = onSkipPrevious) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous"
                )
            }

            FloatingActionButton(
                onClick = onPlayPause,
                containerColor = colorResource(R.color.green_80)
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause"
                )
            }

            IconButton(onClick = onSkipNext) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next")
            }

            IconButton(onClick = onToggleShuffle) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle Mode",
                    tint = if (shuffleEnabled) colorResource(R.color.green_80) else colorResource(R.color.white_100)
                )
            }
        }
    }
}