package com.haloai.ui.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Lightweight markdown renderer supporting paragraphs and fenced code blocks (```)
 * - Code blocks are rendered with monospaced font and a subtle background to emulate syntax blocks.
 * This is intentionally minimal to avoid adding external dependencies in the starter skeleton.
 */
@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier) {
    val parts = splitMarkdown(markdown)
    Column(modifier = modifier) {
        for (p in parts) {
            when (p.type) {
                PartType.PARAGRAPH -> {
                    BasicText(text = p.content, style = TextStyle(fontSize = 15.sp), modifier = Modifier.padding(vertical = 4.dp))
                }
                PartType.CODE -> {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F0F10))
                        .padding(8.dp)) {
                        BasicText(text = p.content.trim(), style = TextStyle(fontSize = 13.sp, fontFamily = FontFamily.Monospace))
                    }
                }
            }
        }
    }
}

private enum class PartType { PARAGRAPH, CODE }
private data class Part(val type: PartType, val content: String)

private fun splitMarkdown(md: String): List<Part> {
    val lines = md.lines()
    val parts = mutableListOf<Part>()
    var i = 0
    val sb = StringBuilder()
    var inCode = false
    val codeSb = StringBuilder()
    while (i < lines.size) {
        val l = lines[i]
        if (l.trim().startsWith("```")) {
            if (!inCode) {
                // flush paragraph
                if (sb.isNotEmpty()) { parts.add(Part(PartType.PARAGRAPH, sb.toString())); sb.clear() }
                inCode = true
            } else {
                // end code block
                parts.add(Part(PartType.CODE, codeSb.toString())); codeSb.clear(); inCode = false
            }
        } else {
            if (inCode) {
                codeSb.appendLine(l)
            } else {
                if (sb.isNotEmpty()) sb.appendLine(l) else sb.append(l)
            }
        }
        i++
    }
    if (sb.isNotEmpty()) parts.add(Part(PartType.PARAGRAPH, sb.toString()))
    return parts
}
