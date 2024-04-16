package com.phoebus.pix.demo.data.model

import com.google.gson.annotations.SerializedName

data class ConsultPixByClientIDResponse(
    @SerializedName("cob_value")
    val cobValue: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tx_id")
    val txId: String,
)