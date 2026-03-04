package com.phoebus.pix.demo.services

import android.util.Log
import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.data.model.PixResponse
import com.phoebus.pix.demo.data.model.RefundByTxIdPixRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class RefundByTxIdService(

) {

    operator fun invoke(
        pixClient: PixClient,
        txId: String,
        printCustomerReceipt: Boolean,
        printMerchantReceipt: Boolean,
        previewCustomerReceipt: Boolean,
        previewMerchantReceipt: Boolean
    ) = callbackFlow {

        try {
            val gson = Gson()
            if (!pixClient.isBound()) {
                trySend(Result.failure(Exception("Applicação não conectada")));
                close()
                return@callbackFlow
            }

            val refundByTxIdPixRequest = RefundByTxIdPixRequest(
                txId,
                printCustomerReceipt,
                printMerchantReceipt,
                previewCustomerReceipt,
                previewMerchantReceipt
            )

            val callback = object : PixClient.RefundPixPaymentCallback {
                override fun onError(response: String?) {
                    response?.let { Log.e("RefundByTxIdService", it) };
                    val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                    trySend(Result.failure(Exception(responseError.errorMessage)));
                    close()
                }

                override fun onSuccess(response: String?) {
                    val pixResponse = gson.fromJson(response, PixResponse::class.java)
                    response?.let { Log.d("RefundByTxIdService", it) };
                    trySend(Result.success(pixResponse));
                    close()
                }
            }

            pixClient.refundPixPayment(
                refundByTxIdPixRequest.toJson(),
                callback
            )
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }

        awaitClose { }
    }
}