package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReadingSettings
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.ArabicMidnightText
import com.example.ui.theme.SoftBlueContainer
import com.example.ui.theme.SoftBluePrimary
import com.example.ui.theme.TranslationCharcoalText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingSettingsBottomSheet(
    settings: ReadingSettings,
    onSettingsChanged: (ReadingSettings) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var currentFontSize by remember(settings.arabicFontSizeSp) {
        mutableFloatStateOf(settings.arabicFontSizeSp)
    }
    var currentLatin by remember(settings.showLatin) {
        mutableStateOf(settings.showLatin)
    }
    var currentTranslation by remember(settings.showTranslation) {
        mutableStateOf(settings.showTranslation)
    }
    var currentAutoPlay by remember(settings.autoPlayNextAyah) {
        mutableStateOf(settings.autoPlayNextAyah)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        modifier = Modifier.testTag("reading_settings_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pengaturan Bacaan & Tampilan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SoftBluePrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = SoftBluePrimary
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Live Arabic Preview Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = SoftBlueContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontFamily = AmiriFontFamily,
                        fontSize = currentFontSize.sp,
                        color = ArabicMidnightText,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Font Size Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ukuran Huruf Arab",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = ArabicMidnightText
                )
                Text(
                    text = "${currentFontSize.toInt()} sp",
                    style = MaterialTheme.typography.labelLarge,
                    color = SoftBluePrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Slider(
                value = currentFontSize,
                onValueChange = {
                    currentFontSize = it
                    onSettingsChanged(
                        settings.copy(
                            arabicFontSizeSp = it,
                            showLatin = currentLatin,
                            showTranslation = currentTranslation,
                            autoPlayNextAyah = currentAutoPlay
                        )
                    )
                },
                valueRange = 20f..44f,
                steps = 11,
                colors = SliderDefaults.colors(
                    thumbColor = SoftBluePrimary,
                    activeTrackColor = SoftBluePrimary
                ),
                modifier = Modifier.fillMaxWidth().testTag("font_size_slider")
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Toggles
            SettingToggleItem(
                title = "Teks Transliterasi Latin",
                subtitle = "Bantuan membaca bagi pemula atau anak-anak",
                checked = currentLatin,
                onCheckedChange = {
                    currentLatin = it
                    onSettingsChanged(settings.copy(showLatin = it))
                }
            )

            SettingToggleItem(
                title = "Terjemahan Bahasa Indonesia",
                subtitle = "Tampilkan arti ayat resmi Kemenag RI",
                checked = currentTranslation,
                onCheckedChange = {
                    currentTranslation = it
                    onSettingsChanged(settings.copy(showTranslation = it))
                }
            )

            SettingToggleItem(
                title = "Putar Ayat Berikutnya Otomatis",
                subtitle = "Lanjut murottal otomatis saat ayat selesai",
                checked = currentAutoPlay,
                onCheckedChange = {
                    currentAutoPlay = it
                    onSettingsChanged(settings.copy(autoPlayNextAyah = it))
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Info Frekuensi Iklan IronSource
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SoftBlueContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Status Iklan Interstitial IronSource",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SoftBluePrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Iklan Interstitial diset untuk tampil setiap membuka 5 surat (kemudian hitungan di-reset ke 0). Hitungan tersimpan secara persisten meski aplikasi ditutup.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TranslationCharcoalText
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = ArabicMidnightText
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TranslationCharcoalText
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SoftBluePrimary
            )
        )
    }
}
