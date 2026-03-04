package com.phoebus.pix.demo.viewmodels

import android.content.Context
import com.phoebus.pix.demo.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.pix.demo.services.CobCreateService
import com.phoebus.pix.demo.utils.CurrencyUtil
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.enum.ChargeStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CobCreateViewModel : ViewModel() {

    private val _flagValue = MutableStateFlow(false)
    private val _printCustomerReceipt = MutableStateFlow(false)
    private val _printMerchantReceipt = MutableStateFlow(false)
    private val _previewMerchantReceipt = MutableStateFlow(true)
    private val _previewCustomerReceipt = MutableStateFlow(true)
    private val _cobValue = MutableStateFlow("")
    private val _pixClientId = MutableStateFlow(UUID.randomUUID().toString())
    private val _dialogMessage = MutableStateFlow<String?>(null)
    private var _errorMessage = MutableStateFlow<String?>(null)

    var cobValue = _cobValue.asStateFlow()
    var flagValue = _flagValue.asStateFlow()
    var printCustomerReceipt = _printCustomerReceipt.asStateFlow()
    var printMerchantReceipt = _printMerchantReceipt.asStateFlow()
    var previewCustomerReceipt = _previewCustomerReceipt.asStateFlow()
    var previewMerchantReceipt = _previewMerchantReceipt.asStateFlow()
    var pixClientId = _pixClientId.asStateFlow()
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

    fun upgradeCobValue(newValue: String) {
        _cobValue.value = newValue
    }

    fun changeFlag() {
        _flagValue.value = !_flagValue.value
    }


    fun sendRequest(pixClient: PixClient, context: Context) {
        changeErrorMessage(null);
        changeDialogMessage(null);

        val currencyUtil = CurrencyUtil()
        val cobCreateService = CobCreateService();

        val valueCob = if (cobValue.value.isNotEmpty()) {
            currencyUtil.moneyStringForRequest(cobValue.value)
        } else null;

        viewModelScope.launch {
            cobCreateService.invoke(
                pixClient,
                valueCob,
                pixClientId.value,
                printCustomerReceipt.value,
                printMerchantReceipt.value,
                previewCustomerReceipt.value,
                previewMerchantReceipt.value
            ).collect { result ->
                when {
                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        if (exception !== null) {
                            changeErrorMessage(exception.message)
                            resetFilds()
                        }
                    }

                    result.isSuccess -> {
                        val response = result.getOrNull()
                        if (response != null) {
                            if (response.status == ChargeStatus.REMOVED_BY_USER || response.status == ChargeStatus.REMOVED_BY_PSP) {
                                changeDialogMessage(context.getString(R.string.cancelled_charge))
                            } else {
                                changeDialogMessage(response.toJson())
                            }
                        }
                    }

                }
            }
        }

    }

    private fun resetFilds() {
        _cobValue.value = ""
        _pixClientId.value = UUID.randomUUID().toString()
        _flagValue.value = false
    }

    private fun isDigit(char: Char): Boolean {
        return char in '0'..'9'
    }

    fun validateInput(value: String): Boolean {
        return value.all { isDigit(it) }
    }
}