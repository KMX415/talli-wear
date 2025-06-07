package com.talliwear.wear.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.talliwear.shared.data.BabyCareActivity
import com.talliwear.shared.data.ActivityType

@Composable
fun ActivitySummaryCard(
    activities: List<BabyCareActivity>,
    modifier: Modifier = Modifier
) {
    val activityCounts = activities.groupBy { it.type }.mapValues { it.value.size }
    val totalActivities = activities.size

    Card(
        onClick = { },
        modifier = modifier
            .fillMaxWidth(),
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = Color(0xFF1A1A1A),
            endBackgroundColor = Color(0xFF2D2D2D)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Today's Activities",
                style = MaterialTheme.typography.caption1,
                color = Color(0xFFBBBBBB),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "$totalActivities",
                style = MaterialTheme.typography.title2,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            if (activityCounts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                
                // Show top 3 activity types
                val topActivities = activityCounts.entries.sortedByDescending { it.value }.take(3)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    topActivities.forEach { (type, count) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = type.emoji,
                                style = MaterialTheme.typography.caption2
                            )
                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.caption3,
                                color = Color(0xFFBBBBBB)
                            )
                        }
                    }
                }
            }
        }
    }
} 