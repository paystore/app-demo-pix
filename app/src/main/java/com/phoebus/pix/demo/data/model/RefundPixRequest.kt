package com.phoebus.pix.demo.data.model

import com.google.gson.annotations.SerializedName

data class RefundPixRequest(
    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean,
    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean
)




