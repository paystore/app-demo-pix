package com.phoebus.pix.demo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.services.ConsultCobService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConsultCobViewModel : ViewModel() {

    private var _errorMessage = MutableStateFlow<String?>(null)
    private val _printCustomerReceipt = MutableStateFlow(false)
    private val _printMerchantReceipt = MutableStateFlow(false)
    private val _previewMerchantReceipt = MutableStateFlow(true)
    private val _previewCustomerReceipt = MutableStateFlow(true)
    private val _dialogMessage = MutableStateFlow<String?>(null)

    var printCustomerReceipt = _printCustomerReceipt.asStateFlow()
    var printMerchantReceipt = _printMerchantReceipt.asStateFlow()
    var previewCustomerReceipt = _previewCustomerReceipt.asStateFlow()
    var previewMerchantReceipt = _previewMerchantReceipt.asStateFlow()
    var dialogMessage = _dialogMessage.asStateFlow()
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

    private fun changeDialogMessage(message: String?) {
        _dialogMessage.value = message
    }

    private fun changeErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    fun sendRequest(pixClient: PixClient, txId: String) {
        changeErrorMessage(null);
        viewModelScope.launch {
            val consultCobService = ConsultCobService();
            consultCobService.invoke(
                pixClient,
                txId,
                printCustomerReceipt.value,
                printMerchantReceipt.value,
                previewCustomerReceipt.value,
                previewMerchantReceipt.value
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        if (response != null) {
                            changeDialogMessage(response.toJson())
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
