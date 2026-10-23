package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SoftBlueGradientEnd
import com.example.ui.theme.SoftBluePrimary
import kotlin.math.cos
import kotlin.math.sin

/**
 * An authentic 8-pointed Islamic Star (Rub el Hizb) badge in soft blue styling.
 */
@Composable
fun IslamicStarBadge(
    number: Int,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    badgeColor: Color = SoftBluePrimary,
    starColor: Color = SoftBlueGradientEnd,
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h / 2f
            val outerRadius = (w.coerceAtMost(h) / 2f) * 0.92f
            val innerRadius = outerRadius * 0.72f

            // Create 8-pointed star path
            val path = Path()
            val points = 16
            for (i in 0 until points) {
                val angle = (i * Math.PI / (points / 2) - Math.PI / 2).toFloat()
                val r = if (i % 2 == 0) outerRadius else innerRadius
                val x = cx + r * cos(angle)
                val y = cy + r * sin(angle)
                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()

            // Fill
            drawPath(path = path, color = badgeColor)

            // Ornate Outline
            drawPath(
                path = path,
                color = starColor,
                style = Stroke(width = 2.dp.toPx())
            )

            // Inner circle accent
            drawCircle(
                color = Color.White.copy(alpha = 0.45f),
                radius = innerRadius * 0.75f,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        Text(
            text = number.toString(),
            color = textColor,
            fontSize = if (number > 99) 11.sp else 13.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
