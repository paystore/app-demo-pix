package com.phoebus.pix.demo.data.model

import com.google.gson.annotations.SerializedName

data class ListPixResponse(
    @SerializedName("cob_value")
    val cobValue: String,
    @SerializedName("pix_client_id")
    val clientId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tx_id")
    val txId: String,
)