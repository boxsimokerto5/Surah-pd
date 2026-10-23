package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AudioPlaybackState
import com.example.model.AudioPlayingInfo
import com.example.ui.theme.SoftBlueGradientEnd
import com.example.ui.theme.SoftBlueGradientStart
import com.example.ui.theme.SoftBlueLight
import com.example.ui.theme.SoftBluePrimary
import com.example.ui.theme.SoftBluePrimaryDark

@Composable
fun MurottalBottomPlayer(
    playbackInfo: AudioPlayingInfo?,
    onTogglePlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onStop: () -> Unit,
    onPlayerClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isVisible = playbackInfo != null && playbackInfo.state != AudioPlaybackState.IDLE

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        if (playbackInfo != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(SoftBlueGradientStart, SoftBlueGradientEnd)
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable(enabled = onPlayerClick != null) { onPlayerClick?.invoke() }
                    .testTag("murottal_bottom_player"),
                color = SoftBluePrimaryDark
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Audio Waveform / Speaker Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SoftBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        if (playbackInfo.state == AudioPlaybackState.BUFFERING) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Audio Murottal",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Text Info
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = playbackInfo.surahName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        val statusText = when (playbackInfo.state) {
                            AudioPlaybackState.BUFFERING -> "Memuat audio..."
                            AudioPlaybackState.PLAYING -> "Memutar: Ayat ${playbackInfo.ayahNumber} dari ${playbackInfo.totalAyahs}"
                            AudioPlaybackState.PAUSED -> "Jeda: Ayat ${playbackInfo.ayahNumber} dari ${playbackInfo.totalAyahs}"
                            AudioPlaybackState.ERROR -> playbackInfo.errorMessage ?: "Koneksi bermasalah"
                            AudioPlaybackState.IDLE -> ""
                        }
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (playbackInfo.state == AudioPlaybackState.ERROR) Color(0xFFFFB4AB) else SoftBlueLight,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Previous
                        IconButton(
                            onClick = onPrevious,
                            modifier = Modifier.size(36.dp).testTag("audio_prev_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Ayat Sebelumnya",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Play / Pause
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { onTogglePlayPause() }
                                .testTag("audio_play_pause_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (playbackInfo.state == AudioPlaybackState.BUFFERING) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = SoftBluePrimaryDark,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = if (playbackInfo.state == AudioPlaybackState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (playbackInfo.state == AudioPlaybackState.PLAYING) "Jeda" else "Putar",
                                    tint = SoftBluePrimaryDark,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        // Next
                        IconButton(
                            onClick = onNext,
                            modifier = Modifier.size(36.dp).testTag("audio_next_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Ayat Selanjutnya",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Stop/Close
                        IconButton(
                            onClick = onStop,
                            modifier = Modifier.size(32.dp).testTag("audio_stop_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup Pemutar",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
