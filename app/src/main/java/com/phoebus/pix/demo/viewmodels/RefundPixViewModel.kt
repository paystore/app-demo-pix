package com.phoebus.pix.demo.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.pix.demo.services.RefundPixService
import com.phoebus.phastpay.sdk.client.PixClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RefundPixViewModel : ViewModel() {

    private var _errorMessage = MutableStateFlow<String?>(null)
    private val _printCustomerReceipt = MutableStateFlow(false)
    private val _printMerchantReceipt = MutableStateFlow(false)
    private val _previewMerchantReceipt = MutableStateFlow(true)
    private val _previewCustomerReceipt = MutableStateFlow(true)
    private val _isSucesss = MutableStateFlow<Boolean>(false)

    var printCustomerReceipt = _printCustomerReceipt.asStateFlow()
    var printMerchantReceipt = _printMerchantReceipt.asStateFlow()
    var previewCustomerReceipt = _previewCustomerReceipt.asStateFlow()
    var previewMerchantReceipt = _previewMerchantReceipt.asStateFlow()
    var isSucesss = _isSucesss.asStateFlow()
    var errorMessage = _errorMessage.asStateFlow()

    fun changePrintCustomerReceipt() {
        _printCustomerReceipt.value = !_printCustomerReceipt.value
    }

    fun changePrintMerchantReceipt() {
        _printMerchantReceipt.value = !_printMerchantReceipt.value
    }

    fun changePreviewCustomerReceipt() {
        _previewCustomerReceipt.value = !_previewCustomerReceipt.value
    }

    fun changePreviewMerchantReceipt() {
        _previewMerchantReceipt.value = !_previewMerchantReceipt.value
    }

    private fun changeIsSuccessMessage(message: Boolean) {
        _isSucesss.value = message
    }

    private fun changeErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    fun request(pixClient: PixClient) {
        changeErrorMessage(null);
        changeIsSuccessMessage(false);
        viewModelScope.launch {
            val refundPixService = RefundPixService();
            refundPixService.invoke(
                pixClient,
                printCustomerReceipt.value,
                printMerchantReceipt.value,
                previewCustomerReceipt.value,
                previewMerchantReceipt.value
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        if (response != null) {
                            changeIsSuccessMessage(response)
                        }
                    }

                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        if (exception !== null) {
                            changeErrorMessage(exception.message)
                        }
                    }
                }
            }
        }
    }
}