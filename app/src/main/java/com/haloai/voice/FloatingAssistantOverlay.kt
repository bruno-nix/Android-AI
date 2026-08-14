package com.haloai.voice

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * FloatingAssistantOverlay: creates a borderless glass sheet overlay with a pulsing gradient stroke.
 * This is a scaffold for the UI. Actual waveform visualizer and microphone handling should be added
 * with proper permission handling for SYSTEM_ALERT_WINDOW.
 */
class FloatingAssistantOverlay(private val context: Context) {
    private var added = false
    private var root: FrameLayout? = null
    private var wm: WindowManager? = null
    private var scope: CoroutineScope? = null

    fun show() {
        if (added) return
        wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        root = FrameLayout(context)
        val tv = TextView(context).apply {
            text = "Halo Listening..."
            setTextColor(Color.WHITE)
            textSize = 14f
            setPadding(32, 24, 32, 24)
            background = GradientDrawable().apply {
                cornerRadius = 22f
                setColor(Color.parseColor("#1C1C1E"))
                setStroke(4, Color.argb(200, 0, 240, 255))
            }
        }
        root!!.addView(tv)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            android.graphics.PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.x = 0
        params.y = 80

        try {
            wm?.addView(root, params)
            added = true
            // Start a simple pulsing animation of stroke color using coroutine
            scope = CoroutineScope(Dispatchers.Main)
            scope?.launch {
                var alpha = 120
                var increasing = true
                while (added) {
                    val stroke = Color.argb(alpha, 0, 240, 255)
                    (tv.background as? GradientDrawable)?.setStroke(4, stroke)
                    if (increasing) { alpha += 8; if (alpha >= 255) increasing = false } else { alpha -= 8; if (alpha <= 80) increasing = true }
                    delay(80)
                }
            }
        } catch (t: Throwable) {
            // permission denied or not allowed; handle gracefully
        }
    }

    fun hide() {
        if (!added) return
        try {
            wm?.removeView(root)
        } catch (_: Throwable) {}
        scope?.cancel()
        added = false
    }
}
