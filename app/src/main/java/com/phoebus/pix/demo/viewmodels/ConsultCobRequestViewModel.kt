package com.phoebus.pix.demo.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.pix.demo.services.consultCobRequestService
import com.phoebus.pix.sdk.PixClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConsultCobRequestViewModel : ViewModel() {

    private var errorMessage: String by mutableStateOf("")
    private val _printCustomerReceipt = MutableStateFlow<Boolean>(true)
    private val _printMerchantReceipt = MutableStateFlow<Boolean>(true)

    var printCustomerReceipt = _printCustomerReceipt.asStateFlow()
    var printMerchantReceipt = _printMerchantReceipt.asStateFlow()

    fun changePrintCustomerReceipt() {
        _printCustomerReceipt.value = !_printCustomerReceipt.value
    }

    fun changePrintMerchantReceipt() {
        _printMerchantReceipt.value = !_printMerchantReceipt.value
    }

    fun sendRequest(pixClient: PixClient, txId: String, context: Context) {

        viewModelScope.launch {
            try {
                consultCobRequestService(
                    pixClient,
                    txId,
                    printCustomerReceipt.value,
                    printMerchantReceipt.value,
                    context
                )
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

}