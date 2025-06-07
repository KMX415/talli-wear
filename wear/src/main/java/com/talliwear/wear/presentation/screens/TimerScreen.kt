package com.talliwear.wear.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*

@Composable
fun TimerScreen(
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⏱️",
                style = MaterialTheme.typography.display1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Timer Screen",
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Coming Soon",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
} 