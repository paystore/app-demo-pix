package com.phoebus.pix.demo.services

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.phoebus.pix.demo.data.model.ConsultCobRequest
import com.phoebus.pix.demo.data.model.PixResponse
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.utils.ResponseUtils
import com.phoebus.pix.sdk.PixClient

fun consultCobRequestService(
    pixClient: PixClient,
    txId: String,
    printCustomerReceipt: Boolean,
    printMerchantReceipt: Boolean,
    context: Context
) {
    val gson: Gson = Gson()

    if (pixClient.isBound() && txId.isNotBlank()) {
        val consultCobRequest = ConsultCobRequest(txId, printCustomerReceipt, printMerchantReceipt)
        val callback = object : PixClient.ConsultByTxIdCallback {
            override fun onError(response: String?) {
                val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                println("Erro $response")
                Toast.makeText(context, responseError.errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(response: String?) {
                println("consultado $response")
                if (response != null) {
                    val responseObject =
                        gson.fromJson(response, PixResponse::class.java)
                    val responseUtils = ResponseUtils()
                    Toast.makeText(
                        context,
                        responseUtils.messageConsultPix(context, responseObject),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        pixClient.consultByTxId(
            gson.toJson(consultCobRequest),
            callback
        )
    }
}