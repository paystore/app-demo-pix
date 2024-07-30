package com.phoebus.pix.demo.services


import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.data.model.PixResponse
import com.phoebus.pix.demo.data.model.RefundPixRequest
import com.phoebus.pix.sdk.PixClient

fun RefundPixService(
    pixClient: PixClient,
    printCustomerReceipt: Boolean,
    printMerchantReceipt: Boolean,
    context: Context

) {

    val gson: Gson = Gson()
    val refundPixRequest = RefundPixRequest( printCustomerReceipt, printMerchantReceipt)
    val callback = object : PixClient.RefundCallback {
        override fun onError(response: String?) {
            println("Erro ao devolver $response")

            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(R.string.refund)
                .setMessage("Não foi possível realizar a devolução.")
                .setNeutralButton("Ok") { _, _ -> }
                .show()

        }

        override fun onSuccess(response: String?) {

            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(R.string.refund)
                .setMessage("Devolução realizada com sucesso.")
                .setNeutralButton("Ok") { _, _ -> }
                .show()

        }

        }

    pixClient.refund(
        gson.toJson(refundPixRequest),
        callback
    )


}