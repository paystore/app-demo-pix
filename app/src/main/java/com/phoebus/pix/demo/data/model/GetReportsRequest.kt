package com.phoebus.pix.demo.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.phoebus.pix.demo.data.enum.ReportType

data class GetReportsRequest(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("report_type")
    val reportType: ReportType
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}