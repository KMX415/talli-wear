package com.talliwear.mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talliwear.shared.data.BabyCareActivity
import com.talliwear.shared.database.ActivityDao
import com.talliwear.mobile.data.DataLayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
        checkWearConnection()
    }

    private fun observeActivities() {
        viewModelScope.launch {
            activityDao.getAllActivities().collect { activities ->
                _uiState.value = _uiState.value.copy(
                    activities = activities,
                    isLoading = false
                )
            }
        }
    }

    private fun checkWearConnection() {
        viewModelScope.launch {
            val isConnected = dataLayerRepository.isWearConnected()
            _uiState.value = _uiState.value.copy(
                isWearConnected = isConnected
            )
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = true,
    val isWearConnected: Boolean = false,
    val activities: List<BabyCareActivity> = emptyList(),
    val error: String? = null
) 