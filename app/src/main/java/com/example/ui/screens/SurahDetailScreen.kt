package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AudioPlaybackState
import com.example.model.AudioPlayingInfo
import com.example.model.Ayah
import com.example.model.ReadingSettings
import com.example.model.Surah
import com.example.ui.components.BismillahBanner
import com.example.ui.components.IslamicStarBadge
import com.example.ui.components.ReadingSettingsBottomSheet
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.ArabicMidnightText
import com.example.ui.theme.LatinSlateText
import com.example.ui.theme.SoftBlueContainer
import com.example.ui.theme.SoftBlueLight
import com.example.ui.theme.SoftBluePrimary
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.SoftPlayingAyahBg
import com.example.ui.theme.SoftPlayingAyahBorder
import com.example.ui.theme.SoftWarmGold
import com.example.ui.theme.TranslationCharcoalText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    surah: Surah,
    targetAyahNumber: Int?,
    settings: ReadingSettings,
    playbackInfo: AudioPlayingInfo?,
    isAyahBookmarked: (Int) -> Boolean,
    isLastRead: (Int) -> Boolean,
    onBackClick: () -> Unit,
    onMarkLastRead: (Ayah) -> Unit,
    onToggleBookmark: (Ayah) -> Unit,
    onPlayAyah: (Ayah) -> Unit,
    onPlayFullSurah: () -> Unit,
    onCopyAyah: (Ayah) -> Unit,
    onShareAyah: (Ayah) -> Unit,
    onUpdateSettings: (ReadingSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }

    var showSettingsSheet by remember { mutableStateOf(false) }
    var showSurahInfo by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Scroll to target ayah if provided (e.g. from Last Read marker)
    LaunchedEffect(targetAyahNumber) {
        if (targetAyahNumber != null) {
            val index = surah.ayahs.indexOfFirst { it.numberInSurah == targetAyahNumber }
            if (index != -1) {
                listState.animateScrollToItem((index + 2).coerceAtLeast(0))
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("surah_detail_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = surah.nameLatin,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ArabicMidnightText
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${surah.nameArabic})",
                                fontFamily = AmiriFontFamily,
                                fontSize = 16.sp,
                                color = SoftBluePrimary
                            )
                        }
                        Text(
                            text = "${surah.revelationType.displayName} • ${surah.totalAyahs} Ayat",
                            style = MaterialTheme.typography.labelSmall,
                            color = TranslationCharcoalText
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("surah_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Beranda",
                            tint = SoftBluePrimary
                        )
                    }
                },
                actions = {
                    // Info toggle
                    IconButton(
                        onClick = { showSurahInfo = !showSurahInfo },
                        modifier = Modifier.testTag("surah_info_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info Surat",
                            tint = if (showSurahInfo) SoftBluePrimary else TranslationCharcoalText
                        )
                    }

                    // Font & Reading settings
                    IconButton(
                        onClick = { showSettingsSheet = true },
                        modifier = Modifier.testTag("reading_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Pengaturan Huruf",
                            tint = SoftBluePrimary
                        )
                    }

                    // Play Full Surah
                    IconButton(
                        onClick = onPlayFullSurah,
                        modifier = Modifier.testTag("play_full_surah_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Putar Murottal Lengkap",
                            tint = SoftBluePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Surah Info & Keutamaan Expandable Card
            if (showSurahInfo) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SoftBlueContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = SoftBluePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Tentang & Keutamaan ${surah.nameLatin}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftBluePrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = surah.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ArabicMidnightText
                            )
                        }
                    }
                }
            }

            // Bismillah Banner (in Soft Blue)
            item {
                BismillahBanner(modifier = Modifier.padding(bottom = 16.dp))
            }

            // Ayahs
            itemsIndexed(surah.ayahs, key = { _, ayah -> ayah.numberInSurah }) { index, ayah ->
                val isCurrentlyPlaying = playbackInfo != null &&
                        playbackInfo.surahNumber == surah.number &&
                        playbackInfo.ayahNumber == ayah.numberInSurah &&
                        playbackInfo.state == AudioPlaybackState.PLAYING

                val isBuffering = playbackInfo != null &&
                        playbackInfo.surahNumber == surah.number &&
                        playbackInfo.ayahNumber == ayah.numberInSurah &&
                        playbackInfo.state == AudioPlaybackState.BUFFERING

                val isMarkedAsLastRead = isLastRead(ayah.numberInSurah)
                val isBookmarked = isAyahBookmarked(ayah.numberInSurah)

                AyahCardItem(
                    ayah = ayah,
                    surahNumber = surah.number,
                    settings = settings,
                    isPlaying = isCurrentlyPlaying,
                    isBuffering = isBuffering,
                    isLastRead = isMarkedAsLastRead,
                    isBookmarked = isBookmarked,
                    onMarkLastRead = { onMarkLastRead(ayah) },
                    onToggleBookmark = { onToggleBookmark(ayah) },
                    onPlayAudio = { onPlayAyah(ayah) },
                    onCopy = { onCopyAyah(ayah) },
                    onShare = { onShareAyah(ayah) }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Bottom spacing for bottom audio player
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showSettingsSheet) {
        ReadingSettingsBottomSheet(
            settings = settings,
            onSettingsChanged = onUpdateSettings,
            onDismiss = { showSettingsSheet = false }
        )
    }
}

@Composable
private fun AyahCardItem(
    ayah: Ayah,
    surahNumber: Int,
    settings: ReadingSettings,
    isPlaying: Boolean,
    isBuffering: Boolean,
    isLastRead: Boolean,
    isBookmarked: Boolean,
    onMarkLastRead: () -> Unit,
    onToggleBookmark: () -> Unit,
    onPlayAudio: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    val cardBgColor by animateColorAsState(
        targetValue = if (isPlaying || isBuffering) {
            SoftPlayingAyahBg
        } else if (isLastRead) {
            SoftBlueLight.copy(alpha = 0.5f)
        } else {
            Color.White
        },
        animationSpec = tween(300),
        label = "cardBgColor"
    )

    val borderColor = if (isPlaying || isBuffering) {
        SoftPlayingAyahBorder
    } else if (isLastRead) {
        SoftBluePrimary
    } else {
        SoftCardBorder
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isPlaying || isBuffering || isLastRead) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("ayah_card_${ayah.numberInSurah}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlaying) 3.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Ayah Action Toolbar Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Ayah Star Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IslamicStarBadge(
                        number = ayah.numberInSurah,
                        size = 36.dp,
                        badgeColor = if (isPlaying) SoftPlayingAyahBorder else SoftBluePrimary
                    )

                    // Last read status pill
                    if (isLastRead) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SoftBluePrimary,
                            contentColor = Color.White
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Terakhir Dibaca",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Mark as Last Read Button
                    IconButton(
                        onClick = onMarkLastRead,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_mark_last_read_${ayah.numberInSurah}")
                    ) {
                        Icon(
                            imageVector = if (isLastRead) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Tandai Terakhir Dibaca",
                            tint = if (isLastRead) SoftBluePrimary else TranslationCharcoalText.copy(alpha = 0.6f)
                        )
                    }

                    // Favorite / Bookmark Button
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_bookmark_${ayah.numberInSurah}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Simpan Favorit",
                            tint = if (isBookmarked) SoftWarmGold else TranslationCharcoalText.copy(alpha = 0.3f)
                        )
                    }

                    // Copy Button
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_copy_${ayah.numberInSurah}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Salin Ayat",
                            tint = TranslationCharcoalText.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_share_${ayah.numberInSurah}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Bagikan Ayat",
                            tint = TranslationCharcoalText.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Audio Play Button
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) SoftBluePrimary else SoftBlueLight)
                            .clickable { onPlayAudio() }
                            .testTag("btn_play_audio_${ayah.numberInSurah}"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isBuffering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = SoftBluePrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Jeda Audio" else "Putar Murottal",
                                tint = if (isPlaying) Color.White else SoftBluePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Text - high contrast crisp deep midnight ink
            Text(
                text = ayah.arabic,
                fontFamily = AmiriFontFamily,
                fontSize = settings.arabicFontSizeSp.sp,
                lineHeight = (settings.arabicFontSizeSp * 1.6f).sp,
                color = ArabicMidnightText,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Latin Transliteration - slate ocean blue
            if (settings.showLatin) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = ayah.latin,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = LatinSlateText,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Indonesian Translation - deep charcoal slate for comfortable reading
            if (settings.showTranslation) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ayah.indonesian,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TranslationCharcoalText,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
