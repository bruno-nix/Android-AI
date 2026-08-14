package com.haloai.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.haloai.core.ai.AiRouter
import com.haloai.data.ai.MockGoogleAiClient
import com.haloai.data.ai.MockGroqClient
import com.haloai.data.ai.MockOpenRouterClient
import com.haloai.data.local.AppDatabase
import com.haloai.data.local.MessageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class Sender { object User : Sender(); object Assistant : Sender() }

data class Message(val id: String = UUID.randomUUID().toString(), val sender: Sender, val text: String, val timestamp: Long = System.currentTimeMillis())

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val router = AiRouter(listOf(MockGoogleAiClient(), MockGroqClient(), MockOpenRouterClient()))

    // Room DB
    private val db: AppDatabase = Room.databaseBuilder(application, AppDatabase::class.java, "halo.db").build()
    private val dao = db.conversationDao()

    init {
        // load recent messages from a default conversation for demo
        viewModelScope.launch {
            try {
                val persisted = dao.getMessagesForConversation("default")
                _messages.value = persisted.map { Message(it.id, if (it.sender == "user") Sender.User else Sender.Assistant, it.content, it.timestamp) }
            } catch (_: Throwable) { }
        }
    }

    fun sendUserMessage(text: String) {
        val userMsg = Message(sender = Sender.User, text = text)
        _messages.value = _messages.value + userMsg
        // persist
        viewModelScope.launch {
            try {
                dao.upsertConversation(com.haloai.data.local.ConversationEntity(id = "default", title = "Default", updatedAt = System.currentTimeMillis()))
                dao.insertMessage(MessageEntity(id = userMsg.id, conversationId = "default", sender = "user", content = userMsg.text, timestamp = userMsg.timestamp))
            } catch (_: Throwable) {}
        }

        // Send to AI in background
        viewModelScope.launch {
            try {
                val response = router.generateWithFailover(text)
                val assistantMsg = Message(sender = Sender.Assistant, text = response)
                _messages.value = _messages.value + assistantMsg
                // persist assistant
                try {
                    dao.insertMessage(MessageEntity(id = assistantMsg.id, conversationId = "default", sender = "assistant", content = assistantMsg.text, timestamp = assistantMsg.timestamp))
                } catch (_: Throwable) {}
            } catch (t: Throwable) {
                val errMsg = Message(sender = Sender.Assistant, text = "Error: ${t.message}")
                _messages.value = _messages.value + errMsg
            }
        }
    }

    // For demo, add quick suggestion handler
    fun useSuggestion(text: String) {
        sendUserMessage(text)
    }
}
