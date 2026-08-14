package com.haloai.permissions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(onRequestPermissions: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text("Welcome to Halo AI\n\nHalo AI needs a few system permissions to provide voice assistant overlays and multimodal features. These include:\n- Microphone (RECORD_AUDIO) for voice input\n- Draw over other apps (SYSTEM_ALERT_WINDOW) for the floating assistant overlay\n- Optionally, set as default assistant (BIND_VOICE_INTERACTION)", modifier = Modifier.padding(bottom = 16.dp))
        Button(onClick = onRequestPermissions) { Text("Continue") }
    }
}
