package com.phoebus.pix.demo.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.services.ConsultGeneralTransactionsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConsultViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private val _printCustomerReceipt = MutableStateFlow(false)
    var printCustomerReceipt = _printCustomerReceipt.asStateFlow()
    private val _printMerchantReceipt = MutableStateFlow(false)
    var printMerchantReceipt = _printMerchantReceipt.asStateFlow()
    private val _previewMerchantReceipt = MutableStateFlow(true)
    var previewMerchantReceipt = _previewMerchantReceipt.asStateFlow()
    private val _previewCustomerReceipt = MutableStateFlow(true)
    var previewCustomerReceipt = _previewCustomerReceipt.asStateFlow()


    private val _dialogMessage = MutableStateFlow<String?>(null)
    var dialogMessage = _dialogMessage.asStateFlow()
    private var _errorMessage = MutableStateFlow<String?>(null)
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

    private fun changeIsLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    private fun changeDialogMessage(message: String?) {
        _dialogMessage.value = message
    }

    private fun changeErrorMessage(message: String?) {
        _errorMessage.value = message
    }


    fun consultTransactions(pixClient: PixClient) {
        viewModelScope.launch {
            changeIsLoading(true);
            changeDialogMessage(null);
            changeErrorMessage(null);

            val consultGeneralTransactionsService = ConsultGeneralTransactionsService()
            consultGeneralTransactionsService.invoke(
                pixClient,
                printCustomerReceipt.value,
                printMerchantReceipt.value,
                previewCustomerReceipt.value,
                previewMerchantReceipt.value
            ).collect { result ->
                when {
                    result.isFailure -> {
                        changeIsLoading(false)
                        val exception = result.exceptionOrNull()
                        if (exception !== null) {
                            changeErrorMessage(exception.message)
                        }
                    }

                    result.isSuccess -> {
                        changeIsLoading(false)
                        val response = result.getOrNull();
                        changeDialogMessage(response?.toJson());
                    }
                }
            }

        }
    }
}
