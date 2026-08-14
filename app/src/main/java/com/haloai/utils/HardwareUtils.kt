package com.haloai.utils

import android.content.Context
import android.os.Build

object HardwareUtils {
    fun supportsRenderEffect(context: Context): Boolean {
        // Basic heuristic: API 31+ likely supports RenderEffect; additional GPU checks can be added later.
        return Build.VERSION.SDK_INT >= 31
    }

    fun isLowRamDevice(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        return am.isLowRamDevice
    }
}
