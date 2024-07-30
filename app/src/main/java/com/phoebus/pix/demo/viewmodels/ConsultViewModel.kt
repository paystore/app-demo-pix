package com.phoebus.pix.demo.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.pix.sdk.PixClient
import com.phoebus.pix.demo.services.consultGeneralTransactions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConsultViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private val _canBack = MutableStateFlow(false)
    val canBack = _canBack.asStateFlow()

    private val _canBackErr = MutableStateFlow(false)
    val canBackErr = _canBackErr.asStateFlow()


    fun consultTransactions(pixClient: PixClient, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            _canBack.value = false
            _canBackErr.value = false


            consultGeneralTransactions(
                pixClient = pixClient,
                context = context,
                onSuccess = { res ->
                    _isLoading.value = false
                    _canBack.value = true

                },
                onError = { err ->
                    _isLoading.value = false
                    _canBackErr.value = true
                }
            )
        }
    }
}
