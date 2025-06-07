package com.talliwear.mobile.data

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.*
import com.talliwear.shared.data.ActivityType
import com.talliwear.shared.data.BabyCareActivity
import com.talliwear.shared.data.DataLayerPaths
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataLayerRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val dataClient: DataClient by lazy { Wearable.getDataClient(context) }
    private val messageClient: MessageClient by lazy { Wearable.getMessageClient(context) }
    private val capabilityClient: CapabilityClient by lazy { Wearable.getCapabilityClient(context) }
    
    companion object {
        private const val TAG = "MobileDataLayer"
    }
    
    suspend fun isWearConnected(): Boolean {
        return try {
            val connectedNodes = capabilityClient
                .getCapability(DataLayerPaths.WEAR_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
                .await()
            connectedNodes.nodes.isNotEmpty()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking wear connection", e)
            false
        }
    }
    
    suspend fun sendActivityToWear(activity: BabyCareActivity) {
        try {
            val request = PutDataMapRequest.create(DataLayerPaths.ACTIVITY_ADDED_PATH).apply {
                dataMap.putString(DataLayerPaths.KEY_ACTIVITY_ID, activity.id)
                dataMap.putString(DataLayerPaths.KEY_ACTIVITY_TYPE, activity.type.name)
                dataMap.putLong(DataLayerPaths.KEY_TIMESTAMP, activity.timestamp)
                dataMap.putString(DataLayerPaths.KEY_NOTES, activity.notes)
                dataMap.putString(DataLayerPaths.KEY_CAREGIVER, activity.caregiverName)
                
                activity.duration?.let { dataMap.putLong(DataLayerPaths.KEY_DURATION, it) }
                activity.quantity?.let { dataMap.putDouble(DataLayerPaths.KEY_QUANTITY, it) }
                activity.unit?.let { dataMap.putString(DataLayerPaths.KEY_UNIT, it) }
            }
            
            dataClient.putDataItem(request.asPutDataRequest().setUrgent()).await()
            Log.d(TAG, "Activity sent to wear: ${activity.type}")
        } catch (e: Exception) {
            Log.e(TAG, "Error sending activity to wear", e)
        }
    }
    
    suspend fun sendSyncCompleteToWear() {
        sendMessageToWear(DataLayerPaths.SYNC_COMPLETE_PATH, "sync_complete".toByteArray())
    }
    
    private suspend fun sendMessageToWear(path: String, data: ByteArray) {
        try {
            val nodes = getConnectedNodes()
            nodes.forEach { node ->
                messageClient.sendMessage(node.id, path, data).await()
                Log.d(TAG, "Message sent to wear: $path")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message to wear: $path", e)
        }
    }
    
    private suspend fun getConnectedNodes(): Set<Node> {
        return try {
            capabilityClient
                .getCapability(DataLayerPaths.WEAR_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
                .await()
                .nodes
        } catch (e: Exception) {
            Log.e(TAG, "Error getting connected nodes", e)
            emptySet()
        }
    }
} 