package com.talliwear.wear.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*

@Composable
fun ConnectionStatusChip(
    isConnected: Boolean,
    onSyncClick: () -> Unit,
    isSyncing: Boolean,
    modifier: Modifier = Modifier
) {
    val chipColor = when {
        isSyncing -> Color(0xFFFF9800) // Orange for syncing
        isConnected -> Color(0xFF4CAF50) // Green for connected
        else -> Color(0xFFF44336) // Red for disconnected
    }
    
    val statusText = when {
        isSyncing -> "Syncing..."
        isConnected -> "Connected"
        else -> "Disconnected"
    }
    
    val emoji = when {
        isSyncing -> "🔄"
        isConnected -> "📱"
        else -> "📱❌"
    }

    Chip(
        onClick = onSyncClick,
        modifier = modifier.fillMaxWidth(),
        enabled = !isSyncing,
        colors = ChipDefaults.chipColors(
            backgroundColor = chipColor.copy(alpha = 0.3f),
            contentColor = Color.White
        ),
        border = ChipDefaults.chipBorder(
            borderColor = chipColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.caption1,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = statusText,
                style = MaterialTheme.typography.caption1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
} 