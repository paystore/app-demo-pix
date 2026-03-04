package com.phoebus.pix.demo.services

import android.util.Log
import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.model.ConsultPixByTxIdRequest
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.data.model.PixResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ConsultCobService() {

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

            val consultCobRequest = ConsultPixByTxIdRequest(
                txId,
                printCustomerReceipt,
                printMerchantReceipt,
                previewCustomerReceipt,
                previewMerchantReceipt
            )
            val callback = object : PixClient.ConsultByTxIdCallback {
                override fun onError(response: String?) {
                    response?.let { Log.e("ConsultCobService", it) };
                    val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                    trySend(Result.failure(Exception(responseError.errorMessage)));
                    close()
                }

                override fun onSuccess(response: String?) {
                    response?.let { Log.d("ConsultCobService", it) };
                    if (!response.isNullOrEmpty()) {
                        val responseObject =
                            gson.fromJson(response, PixResponse::class.java)
                        if (!responseObject.cobValue.isNullOrEmpty() && responseObject.status != null) {
                            trySend(Result.success(responseObject));
                        }
                        close()
                    }
                }
            }
            pixClient.consultByTxId(
                consultCobRequest.toJson(),
                callback
            )


        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }

        awaitClose { }
    }

}