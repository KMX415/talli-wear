package com.talliwear.wear.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.*
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.talliwear.shared.data.ActivityType
import com.talliwear.wear.presentation.MainViewModel
import com.talliwear.wear.presentation.components.QuickActionButton

@Composable
fun QuickActionsScreen(
    onActivityLogged: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Text,
            last = ScalingLazyColumnDefaults.ItemType.Chip
        )
    )

    // Common activities for quick access
    val quickActivities = listOf(
        ActivityType.FEEDING,
        ActivityType.DIAPER_CHANGE,
        ActivityType.SLEEP,
        ActivityType.TUMMY_TIME,
        ActivityType.MEDICATION,
        ActivityType.PUMPING,
        ActivityType.BATH,
        ActivityType.PLAYTIME,
        ActivityType.CRYING
    )

    ScreenScaffold(scrollState = columnState) {
        ScalingLazyColumn(
            columnState = columnState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Quick Log",
                    style = MaterialTheme.typography.title2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            items(quickActivities) { activityType ->
                QuickActionButton(
                    text = activityType.displayName,
                    emoji = activityType.emoji,
                    color = Color(activityType.color),
                    onClick = {
                        viewModel.logActivity(activityType)
                        onActivityLogged()
                    },
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            // Success feedback
            if (uiState.lastLoggedActivity != null) {
                item {
                    Card(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        backgroundPainter = CardDefaults.cardBackgroundPainter(
                            startBackgroundColor = Color(0xFF4CAF50).copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "✓ Logged!",
                                style = MaterialTheme.typography.body1,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "${uiState.lastLoggedActivity!!.type.emoji} ${uiState.lastLoggedActivity!!.type.displayName}",
                                style = MaterialTheme.typography.caption1,
                                color = Color(0xFFBBBBBB),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
} 