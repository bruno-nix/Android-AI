package com.haloai.ui.components

import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.setPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

/**
 * GlassCard: hosts a Compose content inside an Android FrameLayout so we can apply
 * RenderEffect blur on API 31+ to simulate frosted glass. On older devices it falls back
 * to a solid acrylic surface to preserve performance.
 */
@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val context = LocalContext.current
    AndroidView(factory = { ctx ->
        FrameLayout(ctx).apply {
            val bgColor = Color(0xFF1C1C1E)
            setBackgroundColor(bgColor.toArgb())
            // subtle white stroke via foreground overlay could be added later
            setPadding((12 * ctx.resources.displayMetrics.density).toInt())

            if (Build.VERSION.SDK_INT >= 31) {
                try {
                    val radius = 20f
                    val blur = RenderEffect.createBlurEffect(18f, 18f, Shader.TileMode.CLAMP)
                    setRenderEffect(blur)
                } catch (t: Throwable) {
                    // ignore and keep acrylic fallback
                }
            }
            // Add subtle white border stroke at ~10% alpha to match Apple HIG glass edges
            try {
                val strokeColor = android.graphics.Color.argb((0.10 * 255).toInt(), 255, 255, 255)
                val drawable = android.graphics.drawable.GradientDrawable().apply {
                    cornerRadius = 22f * ctx.resources.displayMetrics.density
                    setColor(android.graphics.Color.argb((0.6 * 255).toInt(), 28, 28, 30))
                    setStroke((1 * ctx.resources.displayMetrics.density).toInt(), strokeColor)
                }
                background = drawable
            } catch (_: Throwable) {
            }
        }
    }, update = { view ->
        // Host a ComposeView child to render the passed content
        if (view.childCount == 0) {
            val composeView = object : AbstractComposeView(view.context) {
                @Composable
                override fun Content() {
                    androidx.compose.material3.Surface(color = Color.Transparent, content = content)
                }
            }
            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            view.addView(composeView, lp)
        }
    }, modifier = modifier.clip(RoundedCornerShape(22.dp)))
}
