package com.phoebus.pix.demo.services

import android.util.Log
import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.enum.ChargeStatus
import com.phoebus.pix.demo.data.model.ListPixRequest
import com.phoebus.pix.demo.data.model.ListPixResponse
import com.phoebus.pix.demo.data.model.PixErrorResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.Date

class ListPixPaymentService {

    operator fun invoke(
        pixClient: PixClient, startDateTime: Date, endDateTime: Date
    ) = callbackFlow {

        try {

            val gson = Gson()
            val listStatus: List<ChargeStatus> = listOf(
                ChargeStatus.ACTIVE, ChargeStatus.CONCLUDED,
                ChargeStatus.REFUNDED, ChargeStatus.EXPIRED
            )

            val listPixRequest = ListPixRequest(startDateTime, endDateTime, listStatus, null)

            val callback = object : PixClient.ListPixPaymentCallback {
                override fun onError(response: String?) {
                    response?.let { Log.e("ListPixPaymentService", it) };
                    val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                    trySend(Result.failure(Exception(responseError.errorMessage)));
                    close()
                }

                override fun onSuccess(response: String?) {
                    response?.let { Log.d("ListPixPaymentService", it) };
                    val list = gson.fromJson(response, ListPixResponse::class.java)
                    trySend(Result.success(list));
                    close()
                }
            }

            pixClient.listPixPayment(
                listPixRequest.toJson(),
                callback
            )
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }

        awaitClose { }
    }
}



