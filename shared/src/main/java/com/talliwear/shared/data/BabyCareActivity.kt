package com.talliwear.shared.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity(tableName = "baby_care_activities")
data class BabyCareActivity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: ActivityType,
    val timestamp: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    val duration: Long? = null, // Duration in minutes
    val notes: String = "",
    val quantity: Double? = null, // For feeding amounts, etc.
    val unit: String? = null, // ml, oz, etc.
    val isCompleted: Boolean = true,
    val caregiverName: String = "",
    val synced: Boolean = false // For Data Layer sync status
) {
    fun getDisplayTimestamp(): LocalDateTime {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
    }
    
    fun getFormattedDuration(): String {
        return duration?.let { "${it}m" } ?: ""
    }
    
    fun getFormattedQuantity(): String {
        return if (quantity != null && unit != null) {
            "$quantity $unit"
        } else ""
    }
} 