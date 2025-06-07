package com.talliwear.shared.database

import androidx.room.TypeConverter
import com.talliwear.shared.data.ActivityType

class Converters {
    
    @TypeConverter
    fun fromActivityType(activityType: ActivityType): String {
        return activityType.name
    }
    
    @TypeConverter
    fun toActivityType(activityTypeName: String): ActivityType {
        return ActivityType.fromString(activityTypeName)
    }
} 