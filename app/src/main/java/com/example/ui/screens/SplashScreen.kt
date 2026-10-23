package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.ArabicMidnightText
import com.example.ui.theme.LatinSlateText
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.ui.theme.SoftBlueContainer
import com.example.ui.theme.SoftBlueGradientEnd
import com.example.ui.theme.SoftBlueGradientStart
import com.example.ui.theme.SoftBlueLight
import com.example.ui.theme.SoftBluePrimary
import com.example.ui.theme.SoftIceBackground
import com.example.ui.theme.SoftWarmGold
import com.example.ui.theme.SoftWarmGoldBorder
import com.example.ui.theme.SoftWarmGoldLight
import com.example.ui.theme.TranslationCharcoalText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Prevent back press during splash
    BackHandler {
        // No-op or exit
    }

    val scale = remember { Animatable(0.75f) }
    val alpha = remember { Animatable(0f) }

    // Pulsing subtle glow for the logo
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
        }
        // Delay splash screen for smooth branded entrance
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("splash_screen")
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SoftIceBackground,
                        SoftBlueContainer.copy(alpha = 0.5f),
                        SoftIceBackground
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Decorative background aura circle at top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(240.dp)
                .scale(1.2f)
                .alpha(0.35f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SoftBlueGradientEnd.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Decorative background aura circle at bottom left
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(260.dp)
                .scale(1.2f)
                .alpha(0.3f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SoftWarmGold.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Center Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Emblem
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .alpha(alpha.value),
                contentAlignment = Alignment.Center
            ) {
                // Outer Pulse Glow
                Box(
                    modifier = Modifier
                        .size(148.dp)
                        .scale(1.05f)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    SoftBlueGradientStart.copy(alpha = pulseAlpha),
                                    SoftWarmGold.copy(alpha = pulseAlpha * 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Outer Gold Ring
                Surface(
                    modifier = Modifier
                        .size(136.dp)
                        .shadow(12.dp, CircleShape, spotColor = SoftBluePrimary.copy(alpha = 0.35f))
                        .testTag("splash_logo"),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        SoftWarmGoldBorder,
                                        SoftWarmGoldLight,
                                        SoftWarmGold
                                    )
                                ),
                                shape = CircleShape
                            )
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_quran_app),
                            contentDescription = "Logo Surat Pendek Al-Quran",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Bismillah Calligraphy Banner
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha.value),
                shape = RoundedCornerShape(16.dp),
                color = SoftBlueLight.copy(alpha = 0.8f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    SoftWarmGoldBorder.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontFamily = AmiriFontFamily,
                        fontSize = 24.sp,
                        color = ArabicMidnightText,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Bismillāhir-raḥmānir-raḥīm",
                        fontFamily = PlusJakartaFontFamily,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        color = LatinSlateText,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // App Title
            Text(
                text = "Surat Pendek",
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = SoftBluePrimary,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp,
                modifier = Modifier.alpha(alpha.value)
            )

            Text(
                text = "Al-Qur'an Juz 'Amma",
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = ArabicMidnightText,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Audio Murottal Merdu • Terjemahan • Tajwid",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 13.sp,
                color = TranslationCharcoalText.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Islamic Feature Badges
            Row(
                modifier = Modifier.alpha(alpha.value),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SoftBlueContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftBlueGradientEnd.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = SoftBluePrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "38 Surah Pilihan",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = SoftBluePrimary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SoftWarmGoldLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftWarmGoldBorder.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Audio Full & Ayat",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6B4D08)
                        )
                    }
                }
            }
        }

        // Bottom Footer with Loading Indicator & Quick Enter Button
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
                .alpha(alpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = SoftBluePrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Menyiapkan Ayat & Murottal...",
                    style = MaterialTheme.typography.bodySmall,
                    color = LatinSlateText,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick skip / enter button
            Button(
                onClick = onSplashFinished,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("splash_start_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftBluePrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Mulai Membaca",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Surat Pendek Al-Qur'an • Versi 1.0",
                style = MaterialTheme.typography.labelSmall,
                color = TranslationCharcoalText.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}
