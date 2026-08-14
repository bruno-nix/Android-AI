package com.haloai.data.ai

import com.haloai.core.ai.AiProvider
import kotlinx.coroutines.delay

class MockGoogleAiClient : AiProvider {
    override val name: String = "MockGoogleAI"
    override suspend fun generate(prompt: String): String {
        delay(300) // simulate latency
        // simulate rate limit by throwing if prompt contains special token
        if (prompt.contains("__RATE_LIMIT__")) throw java.io.IOException("429")
        return "[GoogleAI mock response to]: $prompt"
    }
}

class MockGroqClient : AiProvider {
    override val name: String = "MockGroq"
    override suspend fun generate(prompt: String): String {
        delay(200)
        return "[Groq mock response to]: $prompt"
    }
}

class MockOpenRouterClient : AiProvider {
    override val name: String = "MockOpenRouter"
    override suspend fun generate(prompt: String): String {
        delay(250)
        return "[OpenRouter mock response to]: $prompt"
    }
}
