package com.phoebus.pix.demo.viewmodels

import androidx.lifecycle.ViewModel
import com.phoebus.pix.demo.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterPixViewModel: ViewModel() {

    private val _selectedCard = MutableStateFlow<Int>(1)
    private val _startTime = MutableStateFlow<String>("00:00")
    private val _endTime = MutableStateFlow<String>(DateUtils().getCurrentTime())
    private val _startDate = MutableStateFlow<String>(DateUtils().getDate30DaysAgo())
    private val _endDate = MutableStateFlow<String>(DateUtils().getCurrentDate())
    var selectedCard = _selectedCard.asStateFlow()
    var startTime = _startTime.asStateFlow()
    var endTime = _endTime.asStateFlow()
    var startDate = _startDate.asStateFlow()
    var endDate = _endDate.asStateFlow()

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

    fun goingToList( ) {
        if (_selectedCard.value == 1) {
            upgradeStartDate(DateUtils().getCurrentDate())
            upgradeEndDate(DateUtils().getCurrentDate())
        } else if(_selectedCard.value == 2) {
            upgradeStartDate(DateUtils().getFirstDayOfMonth())
            upgradeEndDate(DateUtils().getCurrentDate())
            upgradeStartTime("00:00")
            upgradeEndTime("23:59")
        }
    }
}