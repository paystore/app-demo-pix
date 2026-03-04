package com.phoebus.pix.demo.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.phoebus.pix.demo.data.enum.ChargeStatus
import java.util.Date

data class ListPixRequest(
    @SerializedName("start_date")
    val startDate: Date,
    @SerializedName("end_date")
    val endDate: Date,
    @SerializedName("status")
    val status: List<ChargeStatus>?,
    @SerializedName("value")
    val value: String?,
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}