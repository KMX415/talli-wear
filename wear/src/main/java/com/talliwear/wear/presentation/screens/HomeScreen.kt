package com.talliwear.wear.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.*
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.talliwear.wear.presentation.MainViewModel
import com.talliwear.wear.presentation.components.ActivitySummaryCard
import com.talliwear.wear.presentation.components.ConnectionStatusChip
import com.talliwear.wear.presentation.components.QuickActionButton

@Composable
fun HomeScreen(
    onNavigateToQuickActions: () -> Unit,
    onNavigateToActivityList: () -> Unit,
    onNavigateToTimer: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Text,
            last = ScalingLazyColumnDefaults.ItemType.Chip
        )
    )

    ScreenScaffold(scrollState = columnState) {
        ScalingLazyColumn(
            columnState = columnState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "TalliWear",
                    style = MaterialTheme.typography.title1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            item {
                ConnectionStatusChip(
                    isConnected = uiState.isPhoneConnected,
                    onSyncClick = { viewModel.syncWithPhone() },
                    isSyncing = uiState.isSyncing
                )
            }

            if (!uiState.isLoading && uiState.todaysActivities.isNotEmpty()) {
                item {
                    ActivitySummaryCard(
                        activities = uiState.todaysActivities,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            item {
                QuickActionButton(
                    text = "Quick Log",
                    emoji = "⚡",
                    color = Color(0xFF4CAF50),
                    onClick = onNavigateToQuickActions,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            item {
                QuickActionButton(
                    text = "Timer",
                    emoji = "⏱️",
                    color = Color(0xFF2196F3),
                    onClick = onNavigateToTimer,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            item {
                QuickActionButton(
                    text = "Activities",
                    emoji = "📝",
                    color = Color(0xFF9C27B0),
                    onClick = onNavigateToActivityList,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            if (uiState.lastLoggedActivity != null) {
                item {
                    Card(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        backgroundPainter = CardDefaults.cardBackgroundPainter(
                            startBackgroundColor = Color(0xFF263238)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Last: ${uiState.lastLoggedActivity!!.type.emoji}",
                                style = MaterialTheme.typography.body1,
                                color = Color.White
                            )
                            Text(
                                text = uiState.lastLoggedActivity!!.type.displayName,
                                style = MaterialTheme.typography.caption3,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
} 