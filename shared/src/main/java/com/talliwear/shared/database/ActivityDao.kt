package com.talliwear.shared.database

import androidx.room.*
import com.talliwear.shared.data.BabyCareActivity
import com.talliwear.shared.data.ActivityType
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    
    @Query("SELECT * FROM baby_care_activities ORDER BY timestamp DESC")
    fun getAllActivities(): Flow<List<BabyCareActivity>>
    
    @Query("SELECT * FROM baby_care_activities WHERE id = :id")
    suspend fun getActivityById(id: String): BabyCareActivity?
    
    @Query("SELECT * FROM baby_care_activities WHERE type = :type ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getActivitiesByType(type: ActivityType, limit: Int = 10): List<BabyCareActivity>
    
    @Query("SELECT * FROM baby_care_activities WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    suspend fun getActivitiesInTimeRange(startTime: Long, endTime: Long): List<BabyCareActivity>
    
    @Query("SELECT * FROM baby_care_activities WHERE synced = 0")
    suspend fun getUnsyncedActivities(): List<BabyCareActivity>
    
    @Query("SELECT * FROM baby_care_activities WHERE timestamp >= :todayStart ORDER BY timestamp DESC")
    fun getTodaysActivities(todayStart: Long): Flow<List<BabyCareActivity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: BabyCareActivity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<BabyCareActivity>)
    
    @Update
    suspend fun updateActivity(activity: BabyCareActivity)
    
    @Delete
    suspend fun deleteActivity(activity: BabyCareActivity)
    
    @Query("DELETE FROM baby_care_activities WHERE id = :id")
    suspend fun deleteActivityById(id: String)
    
    @Query("UPDATE baby_care_activities SET synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)
    
    @Query("UPDATE baby_care_activities SET synced = 1 WHERE id IN (:ids)")
    suspend fun markMultipleAsSynced(ids: List<String>)
    
    @Query("DELETE FROM baby_care_activities")
    suspend fun deleteAllActivities()
    
    @Query("SELECT COUNT(*) FROM baby_care_activities WHERE timestamp >= :todayStart")
    suspend fun getTodayActivityCount(todayStart: Long): Int
    
    @Query("SELECT * FROM baby_care_activities WHERE type = :type ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastActivityOfType(type: ActivityType): BabyCareActivity?
} 