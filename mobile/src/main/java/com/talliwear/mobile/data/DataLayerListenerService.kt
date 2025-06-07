package com.talliwear.mobile.data

import android.util.Log
import com.google.android.gms.wearable.*
import com.talliwear.shared.data.ActivityType
import com.talliwear.shared.data.BabyCareActivity
import com.talliwear.shared.data.DataLayerPaths
import com.talliwear.shared.database.ActivityDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DataLayerListenerService : WearableListenerService() {

    @Inject
    lateinit var activityDao: ActivityDao
    
    @Inject
    lateinit var dataLayerRepository: DataLayerRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val TAG = "MobileDataListener"
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        
        dataEvents.forEach { dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    val dataItem = dataEvent.dataItem
                    handleDataItem(dataItem)
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        
        Log.d(TAG, "Message received: ${messageEvent.path}")
        
        when (messageEvent.path) {
            DataLayerPaths.SYNC_REQUEST_PATH -> {
                Log.d(TAG, "Sync requested from wear")
                handleSyncRequest()
            }
            DataLayerPaths.START_TIMER_PATH -> {
                val activityType = String(messageEvent.data)
                Log.d(TAG, "Timer started from wear: $activityType")
                // Handle timer start from wear
            }
            DataLayerPaths.STOP_TIMER_PATH -> {
                val activityType = String(messageEvent.data)
                Log.d(TAG, "Timer stopped from wear: $activityType")
                // Handle timer stop from wear
            }
        }
    }

    private fun handleDataItem(dataItem: DataItem) {
        when (dataItem.uri.path) {
            DataLayerPaths.ACTIVITY_ADDED_PATH -> {
                handleActivityFromWear(dataItem)
            }
        }
    }

    private fun handleActivityFromWear(dataItem: DataItem) {
        serviceScope.launch {
            try {
                val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                val activity = createActivityFromDataMap(dataMap)
                activityDao.insertActivity(activity.copy(synced = true))
                Log.d(TAG, "Activity received from wear: ${activity.type}")
            } catch (e: Exception) {
                Log.e(TAG, "Error handling activity from wear", e)
            }
        }
    }

    private fun handleSyncRequest() {
        serviceScope.launch {
            try {
                // Send all activities to wear
                val activities = activityDao.getUnsyncedActivities()
                activities.forEach { activity ->
                    dataLayerRepository.sendActivityToWear(activity)
                    activityDao.markAsSynced(activity.id)
                }
                
                // Notify wear that sync is complete
                dataLayerRepository.sendSyncCompleteToWear()
                Log.d(TAG, "Sync completed: ${activities.size} activities sent")
            } catch (e: Exception) {
                Log.e(TAG, "Error during sync", e)
            }
        }
    }

    private fun createActivityFromDataMap(dataMap: DataMap): BabyCareActivity {
        return BabyCareActivity(
            id = dataMap.getString(DataLayerPaths.KEY_ACTIVITY_ID) ?: "",
            type = ActivityType.fromString(dataMap.getString(DataLayerPaths.KEY_ACTIVITY_TYPE) ?: ""),
            timestamp = dataMap.getLong(DataLayerPaths.KEY_TIMESTAMP),
            duration = if (dataMap.containsKey(DataLayerPaths.KEY_DURATION)) {
                dataMap.getLong(DataLayerPaths.KEY_DURATION)
            } else null,
            notes = dataMap.getString(DataLayerPaths.KEY_NOTES) ?: "",
            quantity = if (dataMap.containsKey(DataLayerPaths.KEY_QUANTITY)) {
                dataMap.getDouble(DataLayerPaths.KEY_QUANTITY)
            } else null,
            unit = dataMap.getString(DataLayerPaths.KEY_UNIT),
            caregiverName = dataMap.getString(DataLayerPaths.KEY_CAREGIVER) ?: "",
            synced = true
        )
    }
} 