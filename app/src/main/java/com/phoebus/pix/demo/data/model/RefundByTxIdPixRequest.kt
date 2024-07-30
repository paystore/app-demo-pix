package com.phoebus.pix.demo.data.model

import com.google.gson.annotations.SerializedName

data class RefundByTxIdPixRequest(
    @SerializedName("tx_id")
    val txId: String,
    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean,
    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean

)