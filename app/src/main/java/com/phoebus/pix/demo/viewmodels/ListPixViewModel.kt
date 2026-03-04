package com.phoebus.pix.demo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.pix.demo.data.model.ListPixResponse
import com.phoebus.pix.demo.services.ListPixPaymentService
import com.phoebus.phastpay.sdk.client.PixClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class ListPixViewModel : ViewModel() {
    private val _listPix = MutableStateFlow<ListPixResponse?>(null)
    var listPix = _listPix.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    var isLoading = _isLoading.asStateFlow()

    private var _errorMessage = MutableStateFlow<String?>(null)
    var errorMessage = _errorMessage.asStateFlow()

    private fun changeErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    private fun changeIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading;
    }

    private fun changeListPix(listPixItems: ListPixResponse?) {
        _listPix.value = listPixItems;
    }


    fun upgradeListPix(pixClint: PixClient, startDateTime: Date, endDateTime: Date) {

        changeIsLoading(true);
        changeErrorMessage(null);

        viewModelScope.launch {

            val listPixPaymentService = ListPixPaymentService();
            listPixPaymentService.invoke(pixClint, startDateTime, endDateTime).collect { result ->

                when {
                    result.isSuccess -> {
                        changeIsLoading(false);
                        val response = result.getOrNull()
                        if (response != null) {
                            changeListPix(response)
                        }
                    }

                    result.isFailure -> {
                        changeIsLoading(false);
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