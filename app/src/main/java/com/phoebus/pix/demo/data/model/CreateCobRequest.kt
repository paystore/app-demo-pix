package com.phoebus.pix.demo.data.model

import com.google.gson.annotations.SerializedName

data class CreateCobRequest(
    @SerializedName("cob_value")
    val cobValue: String?,
    @SerializedName("pix_client_id")
    val pixClientId: String?,
    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean,
    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean
)