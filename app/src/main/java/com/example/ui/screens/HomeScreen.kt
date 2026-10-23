package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.LastRead
import com.example.model.RevelationType
import com.example.model.Surah
import com.example.ui.FilterCategory
import com.example.ui.components.IslamicStarBadge
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.ArabicMidnightText
import com.example.ui.theme.LatinSlateText
import com.example.ui.theme.SoftBlueContainer
import com.example.ui.theme.SoftBlueGradientEnd
import com.example.ui.theme.SoftBlueGradientStart
import com.example.ui.theme.SoftBlueLight
import com.example.ui.theme.SoftBluePrimary
import com.example.ui.theme.SoftBluePrimaryDark
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.SoftWarmGold
import com.example.ui.theme.SoftWarmGoldBorder
import com.example.ui.theme.TranslationCharcoalText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    surahs: List<Surah>,
    lastRead: LastRead?,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    selectedFilter: FilterCategory,
    onFilterSelected: (FilterCategory) -> Unit,
    onSurahSelected: (Int, Int?) -> Unit,
    onPlaySurah: (Surah) -> Unit,
    onOpenDoaKhatam: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Islamic Header Banner with Soft Blue Morning Mosque Art
        item {
            IslamicHeroHeader(
                onOpenDoaKhatam = onOpenDoaKhatam
            )
        }

        // Penanda Terakhir Dibaca (Soft Blue Gradient Hero Card)
        item {
            LastReadHeroCard(
                lastRead = lastRead,
                onContinueReading = {
                    if (lastRead != null) {
                        onSurahSelected(lastRead.surahNumber, lastRead.ayahNumber)
                    } else if (surahs.isNotEmpty()) {
                        onSurahSelected(surahs.first().number, 1)
                    }
                }
            )
        }

        // Search Bar & Filter Chips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_surah_input"),
                    placeholder = {
                        Text(
                            text = "Cari surat (contoh: An-Naba, An-Nas, Ikhlas)...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TranslationCharcoalText.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Cari Surat",
                            tint = SoftBluePrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Hapus Pencarian",
                                    tint = SoftBluePrimary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = SoftBluePrimary,
                        unfocusedBorderColor = SoftCardBorder
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Categories in Soft Blue
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedFilter == FilterCategory.ALL,
                            onClick = { onFilterSelected(FilterCategory.ALL) },
                            label = { Text("Semua (${surahs.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftBluePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = ArabicMidnightText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedFilter == FilterCategory.ALL,
                                borderColor = SoftCardBorder,
                                selectedBorderColor = SoftBluePrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("filter_all")
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedFilter == FilterCategory.BOOKMARKED,
                            onClick = { onFilterSelected(FilterCategory.BOOKMARKED) },
                            label = { Text("Favorit") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (selectedFilter == FilterCategory.BOOKMARKED) Color.White else SoftWarmGold
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftWarmGold,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = ArabicMidnightText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedFilter == FilterCategory.BOOKMARKED,
                                borderColor = SoftCardBorder,
                                selectedBorderColor = SoftWarmGold
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("filter_bookmarks")
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedFilter == FilterCategory.MAKKIYAH,
                            onClick = { onFilterSelected(FilterCategory.MAKKIYAH) },
                            label = { Text("Makkiyah") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftBluePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = ArabicMidnightText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedFilter == FilterCategory.MAKKIYAH,
                                borderColor = SoftCardBorder,
                                selectedBorderColor = SoftBluePrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("filter_makkiyah")
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedFilter == FilterCategory.MADANIYAH,
                            onClick = { onFilterSelected(FilterCategory.MADANIYAH) },
                            label = { Text("Madaniyah") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftBluePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = ArabicMidnightText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedFilter == FilterCategory.MADANIYAH,
                                borderColor = SoftCardBorder,
                                selectedBorderColor = SoftBluePrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("filter_madaniyah")
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Surat Pendek (Juz 'Amma)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SoftBluePrimary
                )
                Text(
                    text = "${surahs.size} Surat",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TranslationCharcoalText
                )
            }
        }

        // Surah List Items
        if (surahs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada surat yang cocok dengan pencarian",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TranslationCharcoalText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(surahs, key = { it.number }) { surah ->
                SurahListItemCard(
                    surah = surah,
                    onClick = { onSurahSelected(surah.number, null) },
                    onPlayClick = { onPlaySurah(surah) }
                )
            }
        }
    }
}

@Composable
private fun IslamicHeroHeader(
    onOpenDoaKhatam: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ) {
        // Soft blue morning mosque sunrise banner
        Image(
            painter = painterResource(id = R.drawable.img_quran_header_blue),
            contentDescription = "Masjid Pagi Hari",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Soft gradient overlay for high contrast text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SoftBluePrimaryDark.copy(alpha = 0.65f),
                            SoftBluePrimaryDark.copy(alpha = 0.88f)
                        )
                    )
                )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontFamily = AmiriFontFamily,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Surat Pendek Al-Qur'an",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Doa Khatam Button
                Surface(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onOpenDoaKhatam() }
                        .testTag("btn_doa_khatam"),
                    color = Color.White.copy(alpha = 0.22f),
                    contentColor = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = "Doa Khatam",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "Doa",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Wisdom quote
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.28f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "« خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ »",
                        fontFamily = AmiriFontFamily,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "\"Sebaik-baik kalian adalah yang mempelajari Al-Qur'an dan mengajarkannya.\"",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.95f),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
private fun LastReadHeroCard(
    lastRead: LastRead?,
    onContinueReading: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("last_read_hero_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        listOf(SoftBlueGradientStart, SoftBlueGradientEnd)
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
                .background(
                    Brush.linearGradient(
                        listOf(
                            SoftBlueContainer.copy(alpha = 0.6f),
                            Color.White
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SoftBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Terakhir Dibaca",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Terakhir Dibaca",
                            style = MaterialTheme.typography.labelLarge,
                            color = SoftBluePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (lastRead != null) {
                        val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale("id", "ID"))
                        Text(
                            text = dateFormat.format(Date(lastRead.timestamp)),
                            style = MaterialTheme.typography.labelSmall,
                            color = TranslationCharcoalText.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (lastRead != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = lastRead.surahName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = ArabicMidnightText
                            )
                            Text(
                                text = "Ayat ke-${lastRead.ayahNumber}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SoftBluePrimary
                            )
                        }

                        Text(
                            text = lastRead.surahArabic,
                            fontFamily = AmiriFontFamily,
                            fontSize = 28.sp,
                            color = SoftBluePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onContinueReading() }
                            .testTag("btn_continue_reading"),
                        color = SoftBluePrimary,
                        contentColor = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 11.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Lanjutkan Membaca",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Mulai tilawah surat pendek sekarang. Tandai ayat untuk memudahkan Anda melanjutkan bacaan kapan saja.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TranslationCharcoalText
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onContinueReading() }
                            .testTag("btn_start_reading"),
                        color = SoftBluePrimary,
                        contentColor = Color.White
                    ) {
                        Text(
                            text = "Mulai Membaca Surat Al-Fatihah",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SurahListItemCard(
    surah: Surah,
    onClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("surah_card_${surah.number}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SoftCardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Star Badge with Number
            IslamicStarBadge(number = surah.number)

            Spacer(modifier = Modifier.width(14.dp))

            // Surah Latin Name & Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = surah.nameLatin,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ArabicMidnightText
                )
                Text(
                    text = "${surah.meaningId} • ${surah.totalAyahs} Ayat",
                    style = MaterialTheme.typography.bodySmall,
                    color = TranslationCharcoalText
                )
                Row(
                    modifier = Modifier.padding(top = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isMakkiyah = surah.revelationType == RevelationType.MAKKIYAH
                    val badgeBg = if (isMakkiyah) SoftBlueLight else SoftWarmGold.copy(alpha = 0.14f)
                    val badgeColor = if (isMakkiyah) SoftBluePrimary else SoftWarmGold

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeBg
                    ) {
                        Text(
                            text = surah.revelationType.displayName,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quick Play Murottal button
            IconButton(
                onClick = onPlayClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SoftBlueLight)
                    .testTag("quick_play_${surah.number}")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Putar Murottal",
                    tint = SoftBluePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Arabic Calligraphy Script in deep midnight text
            Text(
                text = surah.nameArabic,
                fontFamily = AmiriFontFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = ArabicMidnightText,
                textAlign = TextAlign.End
            )
        }
    }
}
