package com.talliwear.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talliwear.shared.data.BabyCareActivity
import com.talliwear.shared.data.ActivityType
import com.talliwear.shared.database.ActivityDao
import com.talliwear.wear.data.DataLayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val activityDao: ActivityDao,
    private val dataLayerRepository: DataLayerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        observeActivities()
        checkPhoneConnection()
    }

    private fun observeActivities() {
        viewModelScope.launch {
            val todayStart = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .toEpochSecond(ZoneOffset.UTC)

            activityDao.getTodaysActivities(todayStart).collect { activities ->
                _uiState.value = _uiState.value.copy(
                    todaysActivities = activities,
                    isLoading = false
                )
            }
        }
    }

    private fun checkPhoneConnection() {
        viewModelScope.launch {
            val isConnected = dataLayerRepository.isPhoneConnected()
            _uiState.value = _uiState.value.copy(
                isPhoneConnected = isConnected
            )
        }
    }

    fun logActivity(activityType: ActivityType) {
        viewModelScope.launch {
            val activity = BabyCareActivity(
                type = activityType,
                timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                caregiverName = "Watch User"
            )
            
            activityDao.insertActivity(activity)
            dataLayerRepository.sendActivityToPhone(activity)
            
            _uiState.value = _uiState.value.copy(
                lastLoggedActivity = activity
            )
        }
    }

    fun startTimer(activityType: ActivityType) {
        viewModelScope.launch {
            dataLayerRepository.startTimer(activityType)
            _uiState.value = _uiState.value.copy(
                activeTimer = activityType
            )
        }
    }

    fun stopTimer() {
        viewModelScope.launch {
            _uiState.value.activeTimer?.let { timerType ->
                dataLayerRepository.stopTimer(timerType)
                _uiState.value = _uiState.value.copy(
                    activeTimer = null
                )
            }
        }
    }

    fun syncWithPhone() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true)
            
            try {
                dataLayerRepository.requestSyncFromPhone()
                checkPhoneConnection()
            } finally {
                _uiState.value = _uiState.value.copy(isSyncing = false)
            }
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = true,
    val isPhoneConnected: Boolean = false,
    val isSyncing: Boolean = false,
    val todaysActivities: List<BabyCareActivity> = emptyList(),
    val lastLoggedActivity: BabyCareActivity? = null,
    val activeTimer: ActivityType? = null,
    val error: String? = null
) 