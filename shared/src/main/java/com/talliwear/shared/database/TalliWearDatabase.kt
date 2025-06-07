package com.talliwear.shared.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.talliwear.shared.data.BabyCareActivity

@Database(
    entities = [BabyCareActivity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TalliWearDatabase : RoomDatabase() {
    
    abstract fun activityDao(): ActivityDao
    
    companion object {
        @Volatile
        private var INSTANCE: TalliWearDatabase? = null
        
        fun getDatabase(context: Context): TalliWearDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TalliWearDatabase::class.java,
                    "tallywear_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 