package com.haloai.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.haloai.ui.theme.haloSpring
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha

/**
 * Splash screen with a glowing multi-color halo ring that scales up and dissolves into the app.
 * Uses the shared haloSpring animation spec from Theme.kt.
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // scale up with spring physics
        scale.animateTo(1.0f, animationSpec = haloSpring)
        // hold a short moment while halo pulses
        delay(550)
        // dissolve out
        alpha.animateTo(0f, animationSpec = tween(durationMillis = 420))
        // small delay to ensure composable fades before navigating
        delay(60)
        onFinished()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier
            .size(260.dp)
            .scale(scale.value)
            .alpha(alpha.value)
        ) {
            val radius = size.minDimension / 2f
            val strokeWidth = radius * 0.12f
            // Sweep gradient around the circle: cyan -> purple -> pink -> yellow
            val sweep = Brush.sweepGradient(
                colors = listOf(Color(0xFF00F0FF), Color(0xFF7C4DFF), Color(0xFFFF5CA9), Color(0xFFFFDF5C), Color(0xFF00F0FF))
            )

            // Outer glowing ring
            drawCircle(
                brush = sweep,
                radius = radius - strokeWidth / 2f,
                center = Offset(radius, radius),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Subtle inner soft glow
            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = radius * 0.7f,
                center = Offset(radius, radius),
                style = Stroke(width = strokeWidth * 0.6f)
            )
        }
    }
}
