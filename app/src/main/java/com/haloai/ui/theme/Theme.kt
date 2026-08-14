package com.haloai.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color tokens
private val Obsidian = Color(0xFF000000)
private val GlassSurface = Color(0xFF1C1C1E)
private val GlassSurfaceAcrylic = Color(0xFF1C1C1E)
private val Accent = Color(0xFF00F0FF)

val HaloShape = RoundedCornerShape(22.dp)

// Simple SF-like typography tokens (use system fonts as placeholders)
val HaloTypography = Typography(
    bodyLarge = TextStyle(fontSize = 16.sp, letterSpacing = 0.2.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, letterSpacing = 0.15.sp),
    titleLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.2.sp),
    labelLarge = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
)

@Composable
fun HaloTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) {
        // True dark tokens
        androidx.compose.material3.darkColorScheme(
            background = Obsidian,
            surface = GlassSurface,
            primary = Accent
        )
    } else {
        androidx.compose.material3.lightColorScheme(
            background = Obsidian,
            surface = GlassSurfaceAcrylic,
            primary = Accent
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = HaloTypography,
        shapes = Shapes(small = HaloShape, medium = HaloShape, large = HaloShape),
        content = content
    )
}

val haloSpring = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
