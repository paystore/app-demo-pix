package com.phoebus.pix.demo.viewmodels

import androidx.lifecycle.ViewModel
import com.phoebus.pix.demo.data.model.ListPixResponse
import com.phoebus.pix.demo.services.listPixService
import com.phoebus.pix.sdk.PixClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class ListPixViewModel: ViewModel() {
    private val _listPix = MutableStateFlow(emptyArray<ListPixResponse>())
    var listPix = _listPix.asStateFlow()

    fun upgradeListPix(pixClint: PixClient, startDateTime: Date, endDateTime: Date) {
        listPixService(pixClint, startDateTime, endDateTime){ result ->
            _listPix.value = result
        }
    }
}