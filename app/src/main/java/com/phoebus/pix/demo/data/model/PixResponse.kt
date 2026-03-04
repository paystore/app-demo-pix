package com.phoebus.pix.demo.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.phoebus.pix.demo.data.enum.ChargeStatus

data class PixResponse(
    @SerializedName("cob_value")
    val cobValue: String,
    @SerializedName("status")
    var status: ChargeStatus? = ChargeStatus.UNKNOWN,
    @SerializedName("tx_id")
    val txID: String,
    @SerializedName("pix_client_id")
    val pixClientId: String?,
    @SerializedName("date_time")
    val pixDateTime: String,
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}