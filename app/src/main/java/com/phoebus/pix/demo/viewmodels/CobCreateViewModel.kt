package com.phoebus.pix.demo.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.phoebus.pix.demo.services.cobCreateService
import com.phoebus.pix.demo.utils.CurrencyUtil
import com.phoebus.pix.sdk.PixClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CobCreateViewModel : ViewModel() {

    private val _flagValue = MutableStateFlow(false)
    private val _printCustomerReceipt = MutableStateFlow(true)
    private val _printMerchantReceipt = MutableStateFlow(true)
    private val _cobValue = MutableStateFlow("")
    private val _pixClientId = MutableStateFlow(UUID.randomUUID().toString())

    var cobValue = _cobValue.asStateFlow()
    var flagValue = _flagValue.asStateFlow()
    var printCustomerReceipt = _printCustomerReceipt.asStateFlow()
    var printMerchantReceipt = _printMerchantReceipt.asStateFlow()
    var pixClientId = _pixClientId.asStateFlow()

    fun changePrintCustomerReceipt() {
        _printCustomerReceipt.value = !_printCustomerReceipt.value
    }

    fun changePrintMerchantReceipt() {
        _printMerchantReceipt.value = !_printMerchantReceipt.value
    }

    fun upgradeCobValue(newValue: String) {
        _cobValue.value = newValue
    }

    fun changeFlag(){
        _flagValue.value = !_flagValue.value
    }

    fun sendRequest( context: Context, pixClient: PixClient, viewModel: CobCreateViewModel) {
        val currencyUtil = CurrencyUtil()
        try {
            if (_flagValue.value) {
                if(_cobValue.value.isNotEmpty()) {
                    cobCreateService(
                        pixClient,
                        currencyUtil.moneyStringForRequest(cobValue.value),
                        pixClientId.value,
                        printCustomerReceipt.value,
                        printMerchantReceipt.value,
                        context,
                        viewModel
                    )
                } else {
                    Toast.makeText(context, "Informe um valor", Toast.LENGTH_SHORT).show()
                }
            } else {
                cobCreateService(pixClient, null, pixClientId.value,printCustomerReceipt.value,
                    printMerchantReceipt.value, context, viewModel)
            }
        } catch (e: Exception) {
            println(e.message.toString())
        }
    }

    fun resetFilds() {
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