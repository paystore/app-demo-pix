package com.phoebus.pix.demo.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ConsultPixByTxIdRequest(
    @SerializedName("tx_id")
    val txId: String,
    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean,
    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean,
    @SerializedName("preview_customer_receipt")
    val previewCustomerReceipt: Boolean,
    @SerializedName("preview_merchant_receipt")
    val previewMerchantReceipt: Boolean
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}