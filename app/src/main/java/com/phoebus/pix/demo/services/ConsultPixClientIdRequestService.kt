package com.phoebus.pix.demo.services

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.phoebus.pix.demo.data.model.ConsultPixByClientIDResponse
import com.phoebus.pix.demo.data.model.ConsultPixByCorrelationIdRequest
import com.phoebus.pix.demo.data.model.PixErrorResponse
import com.phoebus.pix.demo.utils.ResponseUtils
import com.phoebus.pix.sdk.PixClient

fun consultPixClientIdRequestService(
    pixClient: PixClient,
    pixClientId: String,
    context: Context
) {
    val gson: Gson = Gson()

    if (pixClient.isBound() && pixClientId.isNotBlank()) {
        val consultPixByCorrelationIdRequest = ConsultPixByCorrelationIdRequest(pixClientId)
        val callback = object : PixClient.ConsultByPixClientIdCallback {
            override fun onError(response: String?) {
                val responseError = gson.fromJson(response, PixErrorResponse::class.java)
                println("Erro $response")
                Toast.makeText(context, responseError.errorMessage, Toast.LENGTH_LONG).show()
            }

            override fun onSuccess(response: String?) {
                println("Consultado $response")
                if (response != null) {
                    val responseObject =
                        gson.fromJson(response, ConsultPixByClientIDResponse::class.java)
                    val responseUtils = ResponseUtils()
                    Toast.makeText(
                        context,
                        responseUtils.messageConsultPix(context, responseObject),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        pixClient.consultByPixClientId(
            gson.toJson(consultPixByCorrelationIdRequest),
            callback
        )
    }
}
