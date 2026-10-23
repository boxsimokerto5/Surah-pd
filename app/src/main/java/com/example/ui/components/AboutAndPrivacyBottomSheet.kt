package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.ArabicMidnightText
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

/**
 * Bottom Sheet pop-up ala Google Maps untuk informasi "Tentang Kita" (About Us).
 * Menggunakan sudut membulat khas bagian kanan-kiri atas (topStart & topEnd = 28.dp).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsBottomSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.testTag("about_us_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(scrollState)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SoftBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = SoftBluePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Tentang Kami",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ArabicMidnightText
                        )
                        Text(
                            text = "Geccko Creator • Versi 1.0",
                            style = MaterialTheme.typography.labelMedium,
                            color = SoftBluePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("btn_close_about_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = TranslationCharcoalText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hero Card Aplikasi
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = SoftBluePrimaryDark
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontFamily = AmiriFontFamily,
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Surat Pendek Al-Qur'an",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Juz 'Amma (Juz 30) Lengkap • Edisi 2026",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aplikasi ini didesain khusus dengan antarmuka lembut bernuansa Islami untuk mempermudah umat muslim membaca, mempelajari, mentadaburi, serta menghafalkan 37 surat pendek Al-Qur'an kapan saja dan di mana saja.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.95f),
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Fitur Utama Aplikasi",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = ArabicMidnightText
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Daftar Fitur
            FeatureDetailItem(
                icon = Icons.AutoMirrored.Filled.MenuBook,
                title = "37 Surat Juz 'Amma Lengkap",
                description = "Mencakup surat An-Naba' (78) hingga An-Nas (114) dengan teks ayat Utsmani resmi Kemenag RI."
            )
            FeatureDetailItem(
                icon = Icons.Default.Headphones,
                title = "Audio Murottal Jernih",
                description = "Lantunan ayat demi ayat dan putar penuh surat dari Qari pilihan dengan pemutar audio yang responsif."
            )
            FeatureDetailItem(
                icon = Icons.Default.AutoStories,
                title = "Transliterasi Latin & Terjemahan",
                description = "Membantu pelafalan tajwid yang benar dan memahami arti ayat resmi Kementerian Agama RI."
            )
            FeatureDetailItem(
                icon = Icons.Default.Star,
                title = "Penanda Terakhir Dibaca & Favorit",
                description = "Simpan kemajuan tilawah dan tandai ayat-ayat pilihan yang tersimpan secara lokal dan persisten."
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = SoftCardBorder)
            Spacer(modifier = Modifier.height(14.dp))

            // Info Pengembang
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SoftBlueContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informasi Pengembang",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SoftBluePrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Pengembang: Geccko Creator\n• Package: com.suratpendek.gecckocreator\n• Versi Aplikasi: 1.0 (Rilis Resmi)\n• Kontak Dukungan: proseshidup1101@gmail.com",
                        style = MaterialTheme.typography.bodySmall,
                        color = TranslationCharcoalText,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Semoga aplikasi ini menjadi amal jariyah dan bermanfaat bagi segenap kaum muslimin. Aamiin ya Rabbal 'Alamin.",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = ArabicMidnightText,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

/**
 * Bottom Sheet pop-up ala Google Maps untuk "Kebijakan Privasi" (Privacy Policy).
 * Menggunakan sudut membulat khas bagian kanan-kiri atas (topStart & topEnd = 28.dp).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyBottomSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.testTag("privacy_policy_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(scrollState)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SoftBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = SoftBluePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Kebijakan Privasi",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ArabicMidnightText
                        )
                        Text(
                            text = "Surat Pendek Al-Qur'an • Geccko Creator",
                            style = MaterialTheme.typography.labelMedium,
                            color = SoftBluePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("btn_close_privacy_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = TranslationCharcoalText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Ringkasan Privasi (Hero Highlight)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SoftBlueContainer,
                border = androidx.compose.foundation.BorderStroke(1.dp, SoftCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SoftBluePrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Privasi Anda adalah prioritas utama kami. Kami tidak mengumpulkan data pribadi sensitif seperti identitas, nama, nomor telepon, kontak, maupun lokasi Anda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArabicMidnightText,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Pasal-pasal Kebijakan
            PrivacySectionItem(
                number = "1",
                title = "Penyimpanan Data Lokal di Perangkat",
                description = "Semua data pengguna seperti riwayat Terakhir Dibaca (surat & ayat), daftar ayat Favorit (Bookmark), dan pengaturan bacaan (ukuran huruf & transliterasi) disimpan 100% secara lokal di ponsel Anda melalui SharedPreferences Android. Data tersebut tidak pernah diunggah ke server eksternal kami."
            )

            PrivacySectionItem(
                number = "2",
                title = "Izin Akses Jaringan (Network Permissions)",
                description = "Aplikasi ini memerlukan izin INTERNET dan ACCESS_NETWORK_STATE semata-mata untuk mengalirkan (streaming) file audio murottal yang jernih secara online dan menginisialisasi jaringan iklan pihak ketiga. Aplikasi tidak meminta izin berbahaya seperti lokasi (GPS), kamera, mikrofon, atau kontak."
            )

            PrivacySectionItem(
                number = "3",
                title = "Layanan Iklan Pihak Ketiga (IronSource / LevelPlay)",
                description = "Untuk menunjang pemeliharaan aplikasi secara gratis, aplikasi ini menggunakan SDK periklanan Unity LevelPlay / IronSource untuk menampilkan iklan interstitial setiap kali 5 surat dibuka. SDK ini dapat mengumpulkan pengenal perangkat standar Google Advertising ID sesuai ketentuan kebijakan Google Play dan Unity LevelPlay."
            )

            PrivacySectionItem(
                number = "4",
                title = "Privasi Ramah Keluarga & Anak-Anak",
                description = "Aplikasi ini ditujukan untuk seluruh anggota keluarga, termasuk anak-anak dan pemula yang sedang belajar mengaji. Konten Al-Qur'an disajikan secara murni dan kami mematuhi pedoman perlindungan privasi anak-anak."
            )

            PrivacySectionItem(
                number = "5",
                title = "Hubungi Kami",
                description = "Jika Anda memiliki pertanyaan, saran, atau kekhawatiran terkait kebijakan privasi ini atau aplikasi Surat Pendek Al-Qur'an, jangan ragu untuk menghubungi pengembang Geccko Creator melalui email resmi kami: proseshidup1101@gmail.com."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Terakhir diperbarui: Oktober 2026 • Geccko Creator",
                    style = MaterialTheme.typography.labelSmall,
                    color = TranslationCharcoalText
                )
            }
        }
    }
}

@Composable
private fun FeatureDetailItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(SoftBlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SoftBluePrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = ArabicMidnightText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TranslationCharcoalText,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun PrivacySectionItem(
    number: String,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(SoftBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = ArabicMidnightText
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = TranslationCharcoalText,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 32.dp)
        )
    }
}
