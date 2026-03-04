package com.phoebus.pix.demo.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.enum.ReportType
import com.phoebus.pix.demo.services.GetReportsPixService
import com.phoebus.pix.demo.utils.ConstantsUtils
import com.phoebus.pix.demo.utils.DateUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilterPixViewModel : ViewModel() {

    private val _selectedCard = MutableStateFlow(1)
    private val _startTime = MutableStateFlow("00:00")
    private val _endTime = MutableStateFlow(DateUtils().getCurrentTime())
    private val _startDate = MutableStateFlow(DateUtils().getDate30DaysAgo())
    private val _endDate = MutableStateFlow(DateUtils().getCurrentDate())
    var selectedCard = _selectedCard.asStateFlow()
    var startTime = _startTime.asStateFlow()
    var endTime = _endTime.asStateFlow()
    var startDate = _startDate.asStateFlow()
    var endDate = _endDate.asStateFlow()

    private var _errorMessage = MutableStateFlow<String?>(null)
    var errorMessage = _errorMessage.asStateFlow()

    private val _navigationEventHome = MutableSharedFlow<Boolean>()
    val navigationEventHome: SharedFlow<Boolean> = _navigationEventHome.asSharedFlow()

    private val _selectedFilter = mutableStateOf(ReportType.SUMMARY)
    val selectedFilter: State<ReportType> = _selectedFilter

    fun setSelectedFilter(type: ReportType) {
        _selectedFilter.value = type
    }

    fun getStartDateTimeForNavigation(): String {
        goingToList()
        return "${_startDate.value.replace("/", "-")} ${_startTime.value}"
    }

    fun getEndDateTimeForNavigation(): String {
        goingToList()
        return "${_endDate.value.replace("/", "-")} ${_endTime.value}"
    }

    fun upgradeSelectedCard(newCardId: Int) {
        _selectedCard.value = newCardId
    }

    fun upgradeStartTime(newValue: String) {
        _startTime.value = newValue
    }

    fun upgradeEndTime(newValue: String) {
        _endTime.value = newValue
    }

    fun upgradeStartDate(newValue: String) {
        _startDate.value = newValue
    }

    fun upgradeEndDate(newValue: String) {
        _endDate.value = newValue
    }

    private fun navigateToHome(){
        viewModelScope.launch {
            _navigationEventHome.emit(true)
        }
    }

    private fun goingToList() {
        if (_selectedCard.value == 1) {
            upgradeStartDate(DateUtils().getCurrentDate())
            upgradeEndDate(DateUtils().getCurrentDate())
        } else if (_selectedCard.value == 2) {
            upgradeStartDate(DateUtils().getFirstDayOfMonth())
            upgradeEndDate(DateUtils().getCurrentDate())
            upgradeStartTime("00:00")
            upgradeEndTime("23:59")
        }
    }

    fun getReport(pixClient: PixClient) {
        viewModelScope.launch {
            _errorMessage.value = null;
            val getReportsPixService = GetReportsPixService();
            getReportsPixService.invoke(
                pixClient = pixClient,
                startDate = DateUtils().convertToUtcIso8601(getStartDateTimeForNavigation())
                    ?: "2024-06-01T00:40:01.444Z",
                endDate = DateUtils().convertToUtcIso8601(getEndDateTimeForNavigation())
                    ?: "2024-06-05T00:40:01.444Z",
                reportType = _selectedFilter.value
            ).collect { result ->
                when {
                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        if (exception !== null) {
                            _errorMessage.value = exception.message
                        }
                    };
                    result.isSuccess -> {
                        navigateToHome();
                    }
                }
            }

        }
    }
}