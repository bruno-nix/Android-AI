package com.haloai.core.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Simple AI provider interface and a failover router that tries providers in order.
 */
interface AiProvider {
    val name: String
    suspend fun generate(prompt: String): String
}

class AiRouter(private val providers: List<AiProvider>) {
    suspend fun generateWithFailover(prompt: String): String = withContext(Dispatchers.IO) {
        var lastError: Throwable? = null
        for (p in providers) {
            try {
                return@withContext p.generate(prompt)
            } catch (t: Throwable) {
                lastError = t
                // continue to next provider
            }
        }
        throw lastError ?: RuntimeException("No providers available")
    }
}
