package com.haloai.voice

import android.service.voice.VoiceInteractionService
import android.content.Intent
import android.os.Bundle
import android.util.Log

/**
 * Minimal VoiceInteractionService scaffold. Full implementation requires handling of
 * voice sessions and session UI; this is a placeholder to be referenced from the AndroidManifest.
 */
class HaloVoiceInteractionService : VoiceInteractionService() {
    private val TAG = "HaloVIS"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "HaloVoiceInteractionService created")
    }

    override fun onReady() {
        super.onReady()
        Log.d(TAG, "HaloVoiceInteractionService ready")
    }

    override fun onNewSession(args: Bundle?): Session {
        Log.d(TAG, "Creating new voice interaction session")
        return super.onNewSession(args)
    }

    override fun onStopListening() {
        super.onStopListening()
        Log.d(TAG, "Stopped listening")
    }

    // Hook to start a floating overlay
    fun showFloatingAssistant() {
        FloatingAssistantOverlay(applicationContext).show()
    }

    fun hideFloatingAssistant() {
        FloatingAssistantOverlay(applicationContext).hide()
    }
}
