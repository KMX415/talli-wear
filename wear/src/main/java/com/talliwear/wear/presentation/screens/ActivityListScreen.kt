package com.talliwear.wear.presentation.screens

import androidx.compose.foundation.layout.*
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
import com.talliwear.shared.data.BabyCareActivity
import com.talliwear.wear.presentation.MainViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ActivityListScreen(
    onActivityClick: (String) -> Unit,
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
                    text = "Today's Activities",
                    style = MaterialTheme.typography.title2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            } else if (uiState.todaysActivities.isEmpty()) {
                item {
                    Card(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        backgroundPainter = CardDefaults.cardBackgroundPainter(
                            startBackgroundColor = Color(0xFF424242)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📝",
                                style = MaterialTheme.typography.title3
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "No activities today",
                                style = MaterialTheme.typography.body2,
                                color = Color(0xFFBBBBBB),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(uiState.todaysActivities) { activity ->
                    ActivityListItem(
                        activity = activity,
                        onClick = { onActivityClick(activity.id) },
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityListItem(
    activity: BabyCareActivity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val displayTime = activity.getDisplayTimestamp().format(timeFormatter)

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = Color(activity.type.color).copy(alpha = 0.2f),
            endBackgroundColor = Color(activity.type.color).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = activity.type.emoji,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = activity.type.displayName,
                        style = MaterialTheme.typography.body2,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (activity.duration != null || activity.getFormattedQuantity().isNotEmpty()) {
                        Text(
                            text = "${activity.getFormattedDuration()} ${activity.getFormattedQuantity()}".trim(),
                            style = MaterialTheme.typography.caption3,
                            color = Color(0xFFBBBBBB),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Text(
                text = displayTime,
                style = MaterialTheme.typography.caption1,
                color = Color(0xFFBBBBBB)
            )
        }
    }
} 