package com.talliwear.wear.data

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

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val TAG = "DataLayerListener"
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        
        dataEvents.forEach { dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED, DataEvent.TYPE_DELETED -> {
                    val dataItem = dataEvent.dataItem
                    handleDataItem(dataItem, dataEvent.type == DataEvent.TYPE_DELETED)
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        
        Log.d(TAG, "Message received: ${messageEvent.path}")
        
        when (messageEvent.path) {
            DataLayerPaths.SYNC_COMPLETE_PATH -> {
                Log.d(TAG, "Sync completed from phone")
                // Handle sync completion if needed
            }
            DataLayerPaths.START_TIMER_PATH -> {
                val activityType = String(messageEvent.data)
                Log.d(TAG, "Timer started for: $activityType")
                // Handle timer start from phone
            }
            DataLayerPaths.STOP_TIMER_PATH -> {
                val activityType = String(messageEvent.data)
                Log.d(TAG, "Timer stopped for: $activityType")
                // Handle timer stop from phone
            }
        }
    }

    private fun handleDataItem(dataItem: DataItem, isDeleted: Boolean) {
        when (dataItem.uri.path) {
            DataLayerPaths.ACTIVITY_ADDED_PATH -> {
                if (!isDeleted) {
                    handleActivityAdded(dataItem)
                }
            }
            DataLayerPaths.ACTIVITY_UPDATED_PATH -> {
                if (!isDeleted) {
                    handleActivityUpdated(dataItem)
                }
            }
            DataLayerPaths.ACTIVITY_DELETED_PATH -> {
                handleActivityDeleted(dataItem)
            }
        }
    }

    private fun handleActivityAdded(dataItem: DataItem) {
        serviceScope.launch {
            try {
                val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                val activity = createActivityFromDataMap(dataMap)
                activityDao.insertActivity(activity.copy(synced = true))
                Log.d(TAG, "Activity added from phone: ${activity.type}")
            } catch (e: Exception) {
                Log.e(TAG, "Error handling activity added", e)
            }
        }
    }

    private fun handleActivityUpdated(dataItem: DataItem) {
        serviceScope.launch {
            try {
                val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                val activity = createActivityFromDataMap(dataMap)
                activityDao.updateActivity(activity.copy(synced = true))
                Log.d(TAG, "Activity updated from phone: ${activity.type}")
            } catch (e: Exception) {
                Log.e(TAG, "Error handling activity updated", e)
            }
        }
    }

    private fun handleActivityDeleted(dataItem: DataItem) {
        serviceScope.launch {
            try {
                val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                val activityId = dataMap.getString(DataLayerPaths.KEY_ACTIVITY_ID)
                if (activityId != null) {
                    activityDao.deleteActivityById(activityId)
                    Log.d(TAG, "Activity deleted from phone: $activityId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error handling activity deleted", e)
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