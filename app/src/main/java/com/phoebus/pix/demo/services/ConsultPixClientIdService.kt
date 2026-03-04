package com.phoebus.pix.demo.services

import android.util.Log
import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.model.ConsultPixByClientIdRequest
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.data.model.PixResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ConsultPixClientIdService() {
    operator fun invoke(
        pixClient: PixClient,
        pixClientId: String
    ) = callbackFlow {
        try {
            val gson = Gson()
            if (!pixClient.isBound()) {
                trySend(Result.failure(Exception("Applicação não conectada")));
                close()
                return@callbackFlow
            }

            val consultPixByClientIdRequest = ConsultPixByClientIdRequest(pixClientId)
            val callback = object : PixClient.ConsultByPixClientIdCallback {
                override fun onError(response: String?) {
                    response?.let { Log.e("ConsultPixClientService", it) };
                    val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                    trySend(Result.failure(Exception(responseError.errorMessage)));
                    close()
                }

                override fun onSuccess(response: String?) {
                    response?.let { Log.d("ConsultPixClientService", it) };
                    if (response != null) {
                        val responseObject =
                            gson.fromJson(response, PixResponse::class.java)
                        trySend(Result.success(responseObject));
                    }
                    close()
                }
            }
            pixClient.consultByPixClientId(
                consultPixByClientIdRequest.toJson(),
                callback
            )
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose { }

    }
}
