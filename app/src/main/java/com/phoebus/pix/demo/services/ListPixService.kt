package com.phoebus.pix.demo.services

import com.google.gson.Gson
import com.phoebus.pix.demo.data.model.ListPixRequest
import com.phoebus.pix.demo.data.model.ListPixResponse
import com.phoebus.pix.sdk.PixClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

fun listPixService(pixClient: PixClient, startDateTime: Date, endDateTime: Date, listPix: (Array<ListPixResponse>) -> Unit) {
    val gson: Gson = Gson()

    CoroutineScope(Dispatchers.Main).launch {
        val listPixRequest = ListPixRequest(startDateTime, endDateTime)
        val callback = object : PixClient.ListPixPaymentCallback {
            override fun onError(response: String?) {
                println("Response $response")
            }

            override fun onSuccess(response: String?) {
                println("Response $response")
                val list = gson.fromJson(response, Array<ListPixResponse>::class.java)
                listPix(list)
            }
        }

        pixClient.listPixPayment(
            gson.toJson(listPixRequest),
            callback
        )
    }
}



