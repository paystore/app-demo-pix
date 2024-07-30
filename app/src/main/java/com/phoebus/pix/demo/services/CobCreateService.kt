package com.phoebus.pix.demo.services

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.data.enum.ChargeStatus
import com.phoebus.pix.demo.data.model.CreateCobRequest
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.data.model.PixResponse
import com.phoebus.pix.demo.viewmodels.CobCreateViewModel
import com.phoebus.pix.sdk.PixClient

fun cobCreateService(
    pixClient: PixClient,
    value: String?,
    pixClientId: String,
    printCustomerReceipt: Boolean,
    printMerchantReceipt: Boolean,
    context: Context,
    viewModel: CobCreateViewModel
) {
    val gson: Gson = Gson()
    if (pixClient.isBound()) {
        val createCobRequest =
            CreateCobRequest(value, pixClientId, printCustomerReceipt, printMerchantReceipt)
        val callback = object : PixClient.StartPixPaymentCallback {
            override fun onError(response: String?) {
                println("Pagamento error: $response")

                if (response != null) {
                    val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                    showAlertDialog(context, responseError.errorMessage)
                } else {
                    showAlertDialog(context, "")
                }
                viewModel.resetFilds()
            }

            override fun onSuccess(response: String?) {
                val pixResponse = gson.fromJson(response, PixResponse::class.java)
                println("Pagamento: $pixResponse")

                if (pixResponse != null && pixResponse.status == ChargeStatus.REMOVED_BY_USER ) {
                    Toast.makeText(context, R.string.payment_cancelled, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.payment_successfully_made, Toast.LENGTH_SHORT)
                        .show()
                }
                viewModel.resetFilds()
            }
        }

        pixClient.startPixPayment(
            gson.toJson(createCobRequest),
            callback
        )

    }
}

fun showAlertDialog(context: Context, message: String) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.cob_gen)
    builder.setMessage(message)
    builder.setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}