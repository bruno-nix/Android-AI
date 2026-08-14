package com.haloai.voice

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

/**
 * Lightweight wrapper for Android SpeechRecognizer and TextToSpeech.
 * For initial implementation this is a scaffolding: actual permission checks and lifecycle management
 * should be added when integrating into the Activity/Service.
 */
class VoiceService(private val context: Context) {
    private var tts: TextToSpeech? = null

    fun initTts(onReady: (() -> Unit)? = null) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                onReady?.invoke()
            }
        }
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "halo_tts_${System.currentTimeMillis()}")
    }

    fun destroy() {
        tts?.shutdown()
    }

    // Provide a cold Flow of recognized text from SpeechRecognizer for composability
    fun startListening(): kotlinx.coroutines.flow.Flow<String> = callbackFlow {
        val sr = SpeechRecognizer.createSpeechRecognizer(context)
        val intent = RecognizerIntent().apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) { close(Exception("SpeechRecognizer error: $error")) }
            override fun onResults(results: Bundle?) {
                val list = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!list.isNullOrEmpty()) trySend(list[0])
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        sr.setRecognitionListener(listener)
        sr.startListening(intent)

        awaitClose {
            sr.destroy()
        }
    }
}
