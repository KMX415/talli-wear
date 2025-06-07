package com.talliwear.shared.data

/**
 * Constants for Data Layer API communication between phone and watch
 */
object DataLayerPaths {
    // Data paths for syncing activities
    const val ACTIVITIES_PATH = "/activities"
    const val ACTIVITY_ADDED_PATH = "/activity/added"
    const val ACTIVITY_UPDATED_PATH = "/activity/updated"
    const val ACTIVITY_DELETED_PATH = "/activity/deleted"
    
    // Message paths for real-time communication
    const val START_TIMER_PATH = "/timer/start"
    const val STOP_TIMER_PATH = "/timer/stop"
    const val QUICK_ACTION_PATH = "/quick_action"
    const val SYNC_REQUEST_PATH = "/sync/request"
    const val SYNC_COMPLETE_PATH = "/sync/complete"
    
    // Capability identifiers
    const val PHONE_CAPABILITY = "tallywear_phone"
    const val WEAR_CAPABILITY = "tallywear_wear"
    
    // Data keys
    const val KEY_ACTIVITY_ID = "activity_id"
    const val KEY_ACTIVITY_TYPE = "activity_type"
    const val KEY_TIMESTAMP = "timestamp"
    const val KEY_DURATION = "duration"
    const val KEY_NOTES = "notes"
    const val KEY_QUANTITY = "quantity"
    const val KEY_UNIT = "unit"
    const val KEY_CAREGIVER = "caregiver"
    const val KEY_TIMER_TYPE = "timer_type"
    const val KEY_TIMER_DURATION = "timer_duration"
} 