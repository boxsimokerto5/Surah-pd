package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.SoftBlueGradientEnd
import com.example.ui.theme.SoftBlueGradientStart
import com.example.ui.theme.SoftBluePrimary
import com.example.ui.theme.SoftWarmGold
import com.example.ui.theme.SoftWarmGoldLight

@Composable
fun BismillahBanner(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        SoftBluePrimary,
                        SoftBlueGradientStart,
                        SoftBlueGradientEnd
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.4f),
                        SoftWarmGoldLight,
                        Color.White.copy(alpha = 0.4f)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(vertical = 18.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                fontFamily = AmiriFontFamily,
                fontSize = 26.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                lineHeight = 38.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Bismillāhir-raḥmānir-raḥīm",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = SoftWarmGoldLight,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}
