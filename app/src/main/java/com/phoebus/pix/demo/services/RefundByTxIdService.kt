package com.phoebus.pix.demo.services

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.data.model.PixResponse
import com.phoebus.pix.demo.data.model.RefundByTxIdPixRequest
import com.phoebus.pix.sdk.PixClient

fun refundPixService(
    pixClient: PixClient,
    txId: String,
    printCustomerReceipt: Boolean,
    printMerchantReceipt: Boolean,
    context: Context
) {
    val gson: Gson = Gson()
    val refundByTxIdPixRequest = RefundByTxIdPixRequest(txId, printCustomerReceipt, printMerchantReceipt)
    val callback = object : PixClient.RefundPixPaymentCallback {
        override fun onError(response: String?) {
            println("Erro ao devolver $response")
            val alertDialog = AlertDialog.Builder(context)
            val responseError = gson.fromJson(response, PixErrorResponse::class.java)
            alertDialog.setTitle(R.string.pix_refund)
                .setMessage(responseError.errorMessage)
                .setNeutralButton("Ok") { _, _ -> }
                .show()
        }

        override fun onSuccess(response: String?) {
            val pixResponse = gson.fromJson(response, PixResponse::class.java)
            println("Devolvido $pixResponse")
            Toast.makeText(context, "Pix devolvido!", Toast.LENGTH_SHORT).show()
        }
    }

    pixClient.refundPixPayment(
        gson.toJson(refundByTxIdPixRequest),
        callback
    )
}