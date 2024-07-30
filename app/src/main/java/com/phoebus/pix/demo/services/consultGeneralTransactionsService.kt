package com.phoebus.pix.demo.services

import android.content.Context
import android.widget.Toast
import com.phoebus.pix.sdk.PixClient

fun consultGeneralTransactions(
    pixClient: PixClient,
    context: Context,
    onSuccess: (String?) -> Unit,
    onError: (String?) -> Unit
) {
    if (!pixClient.isBound()) {
        Toast.makeText(context, "PixClient não está conectado.", Toast.LENGTH_SHORT).show()
        return
    }

    val request = """{"print_customer_receipt":false,"print_merchant_receipt":true}"""
    val callback = object : PixClient.ConsultCallback {
        override fun onError(response: String?) {
            Toast.makeText(context, "Erro na consulta: $response", Toast.LENGTH_LONG).show()
            onError(response)
        }

        override fun onSuccess(response: String?) {
            Toast.makeText(context, "Consulta realizada com sucesso.", Toast.LENGTH_LONG).show()
            onSuccess(response)
        }
    }

    pixClient.consult(request, callback)
}
