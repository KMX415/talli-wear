package com.talliwear.shared.data

/**
 * Enum representing different types of baby care activities
 */
enum class ActivityType(
    val displayName: String,
    val emoji: String,
    val color: Int = 0xFF2196F3.toInt() // Default blue
) {
    FEEDING("Feeding", "🍼", 0xFF4CAF50.toInt()),
    DIAPER_CHANGE("Diaper Change", "👶", 0xFF9C27B0.toInt()),
    SLEEP("Sleep", "😴", 0xFF3F51B5.toInt()),
    TUMMY_TIME("Tummy Time", "🤱", 0xFF00BCD4.toInt()),
    MEDICATION("Medication", "💊", 0xFFF44336.toInt()),
    PUMPING("Pumping", "🥛", 0xFF8BC34A.toInt()),
    BATH("Bath", "🛁", 0xFF00BCD4.toInt()),
    PLAYTIME("Playtime", "🧸", 0xFFFF9800.toInt()),
    CRYING("Crying", "😢", 0xFFE91E63.toInt()),
    OTHER("Other", "📝", 0xFF795548.toInt());

    companion object {
        fun fromString(value: String): ActivityType {
            return values().find { it.name == value } ?: OTHER
        }
    }
} 