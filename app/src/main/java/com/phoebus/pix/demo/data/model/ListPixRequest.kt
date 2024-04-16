package com.phoebus.pix.demo.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ListPixRequest(
    @SerializedName("start_date")
    val startDate: Date,
    @SerializedName("end_date")
    val endDate: Date,
)