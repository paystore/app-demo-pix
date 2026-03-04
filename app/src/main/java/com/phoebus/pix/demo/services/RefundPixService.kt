package com.phoebus.pix.demo.services


import android.util.Log
import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.data.model.RefundPixRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class RefundPixService(

) {
    operator fun invoke(
        pixClient: PixClient,
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

            val refundPixRequest = RefundPixRequest(
                printCustomerReceipt,
                printMerchantReceipt,
                previewCustomerReceipt,
                previewMerchantReceipt
            )

            val callback = object : PixClient.RefundCallback {
                override fun onError(response: String?) {
                    response?.let { Log.e("RefundPixService", it) };
                    val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                    trySend(Result.failure(Exception(responseError.errorMessage)));
                    close()
                }

                override fun onSuccess(response: String?) {
                    response?.let { Log.d("RefundPixService", it) };
                    if (!response.isNullOrEmpty()) {
                        trySend(Result.success(true));
                    }
                    close()
                }

            }

            pixClient.refund(
                refundPixRequest.toJson(),
                callback
            )


        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose { }
    }
}