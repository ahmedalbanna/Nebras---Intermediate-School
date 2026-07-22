package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormattedOutputCard(
    rawText: String,
    title: String = "النتيجة الشاملة",
    subject: String = "",
    onSaveToLibrary: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (subject.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = subject,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row {
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(rawText))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "نسخ النص",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (onSaveToLibrary != null) {
                        IconButton(onClick = onSaveToLibrary) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "حفظ في المكتبة",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Body text formatted line by line
            val lines = rawText.split("\n")
            lines.forEach { line ->
                val trimmed = line.trim()
                when {
                    trimmed.startsWith("📌") || trimmed.startsWith("🎯") || trimmed.startsWith("💡") ||
                    trimmed.startsWith("📝") || trimmed.startsWith("🔍") || trimmed.startsWith("📊") ||
                    trimmed.startsWith("🔑") || trimmed.startsWith("⭐") || trimmed.startsWith("✅") ||
                    trimmed.startsWith("⚠️") || trimmed.startsWith("✨") || trimmed.startsWith("📜") ||
                    trimmed.startsWith("🎴") -> {
                        Spacer(modifier = Modifier.height(12.dp))
                        SectionHeaderBadge(text = trimmed)
                    }
                    trimmed.startsWith("-") || trimmed.startsWith("•") || trimmed.startsWith("1.") ||
                    trimmed.startsWith("2.") || trimmed.startsWith("3.") || trimmed.startsWith("4.") || trimmed.startsWith("5.") -> {
                        BulletPointItem(text = trimmed)
                    }
                    else -> {
                        if (trimmed.isNotBlank()) {
                            FormattedLineText(text = trimmed)
                        }
                    }
                }
            }

            if (onSaveToLibrary != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSaveToLibrary,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("حفظ الشرح/الحل في المكتبة للمراجعة 📚")
                }
            }
        }
    }
}

@Composable
fun SectionHeaderBadge(text: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = text.replace("**", ""),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BulletPointItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, end = 8.dp)
                .width(6.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.primary)
        )
        FormattedLineText(
            text = text.removePrefix("-").removePrefix("•").trim(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FormattedLineText(text: String, modifier: Modifier = Modifier) {
    // Basic Markdown bold **parser**
    val formatted = buildAnnotatedString {
        val parts = text.split("**")
        parts.forEachIndexed { index, part ->
            if (index % 2 == 1) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                    append(part)
                }
            } else {
                append(part)
            }
        }
    }

    Text(
        text = formatted,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 22.sp,
        modifier = modifier.padding(vertical = 2.dp)
    )
}
