package com.phoebus.pix.demo.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.pix.demo.services.consultPixClientIdRequestService
import com.phoebus.pix.sdk.PixClient
import kotlinx.coroutines.launch

class ConsultPixClientIdRequestViewModel : ViewModel() {

    private var erroMessage: String by mutableStateOf("")

    fun sendRequest(pixClient: PixClient, clientId: String, context: Context) {

        viewModelScope.launch {
            try {
                consultPixClientIdRequestService(
                    pixClient,
                    clientId,
                    context
                )
            } catch (e: Exception) {
                erroMessage = e.message.toString()
            }
        }
    }

}