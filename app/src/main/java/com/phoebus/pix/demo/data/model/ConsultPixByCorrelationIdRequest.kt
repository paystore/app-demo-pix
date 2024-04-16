package com.phoebus.pix.demo.data.model

import com.google.gson.annotations.SerializedName

data class ConsultPixByCorrelationIdRequest(
    @SerializedName("pix_client_id")
    val pixClientId: String
)