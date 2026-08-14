package com.haloai.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haloai.ui.components.GlassCard
import com.haloai.ui.theme.HaloShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.haloai.ui.markdown.MarkdownText

@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
    val messages by viewModel.messages.collectAsState()
    var input by remember { mutableStateOf(TextFieldValue("")) }
    val haptic = LocalHapticFeedback.current

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat feed
            LazyColumn(modifier = Modifier.weight(1f).padding(12.dp), reverseLayout = false) {
                items(messages, key = { it.id }) { msg ->
                    MessageBubble(msg)
                    Spacer(Modifier.height(8.dp))
                }
            }

            // Quick suggestions
            Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val suggestions = listOf("Research Topic", "Analyze Image", "Summarize Text")
                suggestions.forEach { s ->
                    AssistChip(onClick = { viewModel.useSuggestion(s) }) {
                        Text(s)
                    }
                }
            }

            // Input bar
            GlassCard(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {

                    // Attachment icon placeholder
                    IconButton(onClick = { /* TODO: open photo picker */ }) {
                        // Use a default icon fallback when resources are not available
                        Icon(Icons.Default.AttachFile, contentDescription = "Attach")
                    }

                    Spacer(Modifier.width(8.dp))

                    // Text input
                    Box(modifier = Modifier.weight(1f)) {
                        BasicTextField(value = input, onValueChange = { input = it }, modifier = Modifier.fillMaxWidth(), singleLine = false)
                    }

                    Spacer(Modifier.width(8.dp))

                    // Send button
                    Button(onClick = {
                        val text = input.text.trim()
                        if (text.isNotEmpty()) {
                            viewModel.sendUserMessage(text)
                            input = TextFieldValue("")
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                        }
                    }) {
                        Text("Send")
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(msg: Message) {
    val isUser = msg.sender is Sender.User
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start) {
        GlassCard(modifier = Modifier.widthIn(max = 320.dp).padding(4.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = when (msg.sender) { is Sender.User -> "You"; else -> "Halo" }, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(Modifier.height(6.dp))
                if (msg.sender is Sender.Assistant) {
                    MarkdownText(markdown = msg.text)
                } else {
                    Text(text = msg.text, maxLines = Int.MAX_VALUE)
                }
            }
        }
    }
}
