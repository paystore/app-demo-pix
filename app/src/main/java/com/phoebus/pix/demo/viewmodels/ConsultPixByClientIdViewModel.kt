package com.phoebus.pix.demo.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.model.PixResponse
import com.phoebus.pix.demo.services.ConsultPixClientIdService
import kotlinx.coroutines.launch

class ConsultPixByClientIdViewModel : ViewModel() {

    var erroMessage: String? by mutableStateOf(null)
    var successMessage: PixResponse? by mutableStateOf(null)

    fun sendRequest(pixClient: PixClient, clientId: String) {
        erroMessage = null;
        successMessage = null;
        viewModelScope.launch {
            val consultCobRequestService = ConsultPixClientIdService();
            consultCobRequestService.invoke(
                pixClient,
                clientId
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        successMessage = response
                    }

                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        if (exception !== null) {
                            erroMessage = exception.message
                        }
                    }

                }
            }

        }
    }

}