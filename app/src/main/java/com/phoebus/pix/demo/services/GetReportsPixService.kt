package com.phoebus.pix.demo.services


import android.util.Log
import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.data.enum.ReportType
import com.phoebus.pix.demo.data.model.GetReportsRequest
import com.phoebus.pix.demo.data.model.PixErrorResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class GetReportsPixService() {

    operator fun invoke(
        pixClient: PixClient,
        startDate: String,
        endDate: String,
        reportType: ReportType
    ) = callbackFlow {

        try {
            val gson = Gson()
            if (!pixClient.isBound()) {
                trySend(Result.failure(Exception("Applicação não conectada")));
                close()
                return@callbackFlow
            }
            val getReportsRequest = GetReportsRequest(startDate, endDate, reportType)
            val callback = object : PixClient.GetReportsCallback {
                override fun onError(response: String?) {
                    response?.let { Log.e("ReportPixService", it) };
                    val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                    trySend(Result.failure(Exception(responseError.errorMessage)));
                    close()
                }

                override fun onSuccess(response: String?) {
                    response?.let { Log.d("ReportPixService", it) };
                    trySend(Result.success(true));
                    close()
                }
            }
            pixClient.getReports(
                getReportsRequest.toJson(),
                callback
            )


        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }

        awaitClose { }
    }
}