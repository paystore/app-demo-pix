package com.phoebus.pix.demo.services

import android.util.Log
import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.model.CreateCobRequest
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.data.model.PixResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class CobCreateService(
) {

    operator fun invoke(
        pixClient: PixClient,
        value: String?,
        pixClientId: String,
        printCustomerReceipt: Boolean,
        printMerchantReceipt: Boolean,
        previewCustomerReceipt: Boolean,
        previewMerchantReceipt: Boolean
    ) = callbackFlow {

        try {
            val gson: Gson = Gson()
            if (!pixClient.isBound()) {
                trySend(Result.failure(Exception("Applicação não conectada")));
                close()
                return@callbackFlow
            }

            val createCobRequest =
                CreateCobRequest(
                    value,
                    pixClientId,
                    printCustomerReceipt,
                    printMerchantReceipt,
                    previewCustomerReceipt,
                    previewMerchantReceipt
                )

            val callback = object : PixClient.StartPixPaymentCallback {
                override fun onError(response: String?) {
                    response?.let { Log.e("CreateCobService", it) };
                    val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                    trySend(Result.failure(Exception(responseError.errorMessage)));
                    close()
                }

                override fun onSuccess(response: String?) {
                    val pixResponse = gson.fromJson(response, PixResponse::class.java)
                    response?.let { Log.d("CreateCobService", it) };
                    trySend(Result.success(pixResponse));
                    close()
                }
            }

            pixClient.startPixPayment(
                createCobRequest.toJson(),
                callback
            )
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }

        awaitClose { }
    }

}


//                if (pixResponse != null && pixResponse.status == ChargeStatus.REMOVED_BY_USER ) {
//                    Toast.makeText(context, R.string.payment_cancelled, Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(context, R.string.payment_successfully_made, Toast.LENGTH_SHORT)
//                        .show()
//                }
//                viewModel.resetFilds()

