package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BismillahBanner
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.ArabicMidnightText
import com.example.ui.theme.LatinSlateText
import com.example.ui.theme.SoftBluePrimary
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.TranslationCharcoalText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoaKhatamScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("doa_khatam_screen"),
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Doa Khatam Al-Qur'an",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ArabicMidnightText
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("doa_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Beranda",
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 140.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BismillahBanner()
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "Doa Senandung Al-Qur'an",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SoftBluePrimary
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Arabic in high contrast
                        Text(
                            text = "اللَّهُمَّ ارْحَمْنِي بِالْقُرْآنِ ، وَاجْعَلْهُ لِي إِمَامًا وَنُورًا وَهُدًى وَرَحْمَةً ، اللَّهُمَّ ذَكِّرْنِي مِنْهُ مَا نَسِيتُ ، وَعَلِّمْنِي مِنْهُ مَا جَهِلْتُ ، وَارْزُقْنِي تِلَاوَتَهُ آنَاءَ اللَّيْلِ وَأَطْرَافَ النَّهَارِ ، وَاجْعَلْهُ لِي حُجَّةً يَا رَبَّ الْعَالَمِينَ",
                            fontFamily = AmiriFontFamily,
                            fontSize = 24.sp,
                            lineHeight = 40.sp,
                            textAlign = TextAlign.End,
                            color = ArabicMidnightText,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Latin in slate
                        Text(
                            text = "Allāhummarḥamnī bil-Qur'ān, waj'al-hu lī imāmaw wa nūraw wa hudaw wa raḥmah. Allāhumma żakkirnī min-hu mā nasītu, wa 'allimnī min-hu mā jahiltu, warzuqnī tilāwatahū ānā'al-laili wa aṭrāfan-nahār, waj'al-hu lī ḥujjatan yā rabbal-'ālamīn.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = LatinSlateText,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Indonesian in charcoal
                        Text(
                            text = "\"Ya Allah, rahmatilah aku dengan Al-Qur'an. Jadikanlah ia bagiku sebagai pemimpin, cahaya, petunjuk, dan rahmat. Ya Allah, ingatkanlah aku apa yang aku lupa darinya, ajarkanlah aku apa yang belum aku ketahui darinya, dan anugerahkanlah kepadaku kemampuan untuk membacanya sepanjang malam dan siang hari. Dan jadikanlah ia sebagai pembela (penolong) bagiku, wahai Tuhan semesta alam.\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TranslationCharcoalText,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "Doa Sebelum Membaca Al-Qur'an",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SoftBluePrimary
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "اللَّهُمَّ افْتَحْ عَلَيَّ حِكْمَتَكَ وَانْشُرْ عَلَيَّ رَحْمَتَكَ وَذَكِّرْنِي مَا نَسِيتُ يَا ذَا الْجَلَالِ وَالإِكْرَامِ",
                            fontFamily = AmiriFontFamily,
                            fontSize = 24.sp,
                            lineHeight = 40.sp,
                            textAlign = TextAlign.End,
                            color = ArabicMidnightText,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Allāhummaftaḥ 'alayya ḥikmataka wansyur 'alayya raḥmataka wa żakkirnī mā nasītu yā żal-jalāli wal-ikrām.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = LatinSlateText,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "\"Ya Allah, bukakanlah hikmah-Mu kepadaku, bentangkanlah rahmat-Mu atasku, dan ingatkanlah aku terhadap apa yang aku lupa, wahai Dzat Pemilik Keagungan dan Kemuliaan.\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TranslationCharcoalText,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}
